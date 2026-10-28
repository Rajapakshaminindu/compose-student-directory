package com.example.activity_03.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.activity_03.model.Student
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * ViewModel responsible for all student-directory data operations.
 *
 * Follows the MVVM pattern:
 *  - Exposes immutable [StateFlow]s that the Compose UI observes.
 *  - Encapsulates all Firebase Firestore reads/writes.
 *  - Survives configuration changes (screen rotation, theme switch, etc.)
 *
 * ## State overview
 * | StateFlow        | Purpose                                         |
 * |------------------|-------------------------------------------------|
 * | [students]       | Live list of students from Firestore            |
 * | [isLoading]      | True while any async operation is in progress   |
 * | [errorMessage]   | Non-null when an operation fails                |
 * | [searchQuery]    | Current search filter string                    |
 * | [filteredStudents] | Derived list after applying [searchQuery]     |
 */
class StudentViewModel : ViewModel() {

    // ── Firestore ────────────────────────────────────────────────────────────
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val collection get() = db.collection(Student.COLLECTION)

    /** Keeps a reference so we can remove the Firestore snapshot listener on [onCleared]. */
    private var listenerRegistration: ListenerRegistration? = null

    // ── Exposed state ────────────────────────────────────────────────────────

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    /** Complete, unfiltered list of students from Firestore (ordered by name). */
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    /** True while an async Firestore operation is running. */
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    /** Non-null when the latest Firestore operation ended with an error. */
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    /** Current search/filter text typed by the user. */
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredStudents = MutableStateFlow<List<Student>>(emptyList())
    /**
     * Filtered subset of [students] that matches [searchQuery].
     * The match is case-insensitive and checks both name and studentId.
     */
    val filteredStudents: StateFlow<List<Student>> = _filteredStudents.asStateFlow()

    // ── Init ─────────────────────────────────────────────────────────────────

    init {
        startRealtimeListener()
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Attaches a Firestore snapshot listener that keeps [_students] in sync
     * with the remote collection in real-time, without any explicit polling.
     */
    private fun startRealtimeListener() {
        _isLoading.value = true
        listenerRegistration = collection
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) {
                    _errorMessage.value = "Failed to load students: ${error.message}"
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects(Student::class.java) ?: emptyList()
                _students.value = list
                applyFilter()
            }
    }

    /** Re-applies the current [_searchQuery] against the latest [_students] list. */
    private fun applyFilter() {
        val query = _searchQuery.value.trim().lowercase()
        _filteredStudents.value = if (query.isEmpty()) {
            _students.value
        } else {
            _students.value.filter { student ->
                student.name.lowercase().contains(query) ||
                        student.studentId.lowercase().contains(query) ||
                        student.grade.lowercase().contains(query) ||
                        student.email.lowercase().contains(query)
            }
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Updates the search query and immediately filters the student list.
     *
     * @param query The new search string entered by the user.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applyFilter()
    }

    /** Clears any displayed error so the UI can dismiss the error banner. */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Adds a new student document to Firestore.
     *
     * The [student] parameter should be constructed via `Student.empty().copy(…)`.
     * [createdAt] and [updatedAt] timestamps are set automatically.
     *
     * @param student The student record to persist.
     * @param onSuccess Optional callback invoked on the main thread after success.
     * @param onError   Optional callback invoked on the main thread with the error message.
     */
    fun addStudent(
        student: Student,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val newStudent = student.copy(
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                collection.add(newStudent.toMap()).await()
                onSuccess?.invoke()
            } catch (e: Exception) {
                val msg = "Failed to add student: ${e.message}"
                _errorMessage.value = msg
                onError?.invoke(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Updates an existing student document in Firestore.
     *
     * Only fields contained in [Student.toMap] are written; the Firestore document
     * ID is taken from [student.id].
     *
     * @param student The updated student record. Must have a valid [Student.id].
     * @param onSuccess Optional callback invoked on the main thread after success.
     * @param onError   Optional callback invoked on the main thread with the error message.
     */
    fun updateStudent(
        student: Student,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        if (student.id.isBlank()) {
            _errorMessage.value = "Cannot update student: missing document ID"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val updatedMap = student.withUpdatedTimestamp().toMap()
                collection.document(student.id).update(updatedMap).await()
                onSuccess?.invoke()
            } catch (e: Exception) {
                val msg = "Failed to update student: ${e.message}"
                _errorMessage.value = msg
                onError?.invoke(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Deletes a student document from Firestore.
     *
     * @param studentId The Firestore document ID of the student to delete.
     * @param onSuccess Optional callback invoked on the main thread after success.
     * @param onError   Optional callback invoked on the main thread with the error message.
     */
    fun deleteStudent(
        studentId: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        if (studentId.isBlank()) {
            _errorMessage.value = "Cannot delete student: missing document ID"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                collection.document(studentId).delete().await()
                onSuccess?.invoke()
            } catch (e: Exception) {
                val msg = "Failed to delete student: ${e.message}"
                _errorMessage.value = msg
                onError?.invoke(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fetches a single student by Firestore document ID (one-shot, not streamed).
     *
     * @param studentId The Firestore document ID to fetch.
     * @return The [Student] if found, or null if the document does not exist.
     */
    suspend fun getStudentById(studentId: String): Student? {
        return try {
            val snapshot = collection.document(studentId).get().await()
            snapshot.toObject(Student::class.java)
        } catch (e: Exception) {
            _errorMessage.value = "Failed to fetch student: ${e.message}"
            null
        }
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        // Detach the Firestore listener to prevent memory leaks after the ViewModel is destroyed.
        listenerRegistration?.remove()
    }
}
