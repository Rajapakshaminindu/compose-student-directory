package com.example.activity_03.repository

import com.example.activity_03.model.Student
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository interface defining data access contracts for Student entities.
 * Decouples business logic and ViewModels from concrete backend data sources.
 */
interface StudentRepository {
    /** Observe students as a real-time reactive flow. */
    fun getStudentsFlow(): Flow<List<Student>>

    /** Fetch single student by id. */
    suspend fun getStudent(id: String): Result<Student?>

    /** Add a new student record. */
    suspend fun addStudent(student: Student): Result<String>

    /** Update an existing student record. */
    suspend fun updateStudent(student: Student): Result<Unit>

    /** Delete a student record by ID. */
    suspend fun deleteStudent(id: String): Result<Unit>
}

/**
 * Concrete implementation of [StudentRepository] using Google Cloud Firestore.
 */
class FirestoreStudentRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : StudentRepository {

    private val collection get() = firestore.collection(Student.COLLECTION)

    override fun getStudentsFlow(): Flow<List<Student>> = callbackFlow {
        val listener: ListenerRegistration = collection
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val students = snapshot?.toObjects(Student::class.java) ?: emptyList()
                trySend(students)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getStudent(id: String): Result<Student?> = runCatching {
        val snapshot = collection.document(id).get().await()
        snapshot.toObject(Student::class.java)
    }

    override suspend fun addStudent(student: Student): Result<String> = runCatching {
        val docRef = collection.add(student.toMap()).await()
        docRef.id
    }

    override suspend fun updateStudent(student: Student): Result<Unit> = runCatching {
        require(student.id.isNotBlank()) { "Student ID must not be blank for update" }
        collection.document(student.id).update(student.withUpdatedTimestamp().toMap()).await()
        Unit
    }

    override suspend fun deleteStudent(id: String): Result<Unit> = runCatching {
        require(id.isNotBlank()) { "Student ID must not be blank for delete" }
        collection.document(id).delete().await()
        Unit
    }
}
