package com.example.activity_03.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.activity_03.model.Student
import com.example.activity_03.viewmodel.StudentViewModel

// ──────────────────────────────────────────────────────────────────────────────
// StudentDirectoryScreen – root composable
// ──────────────────────────────────────────────────────────────────────────────

/**
 * Main screen for the Student Directory application.
 *
 * Displays a searchable, scrollable list of students fetched from Firestore in
 * real-time. A floating action button opens the [StudentFormDialog] so the user
 * can add new students. Each list card provides inline Edit and Delete actions.
 *
 * @param viewModel The [StudentViewModel] powering this screen (injected by Compose).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDirectoryScreen(
    viewModel: StudentViewModel = viewModel()
) {
    val students       by viewModel.filteredStudents.collectAsStateWithLifecycle()
    val isLoading      by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage   by viewModel.errorMessage.collectAsStateWithLifecycle()
    val searchQuery    by viewModel.searchQuery.collectAsStateWithLifecycle()

    // Dialog visibility and the student being edited (null = add mode).
    var showDialog     by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    // Confirmation dialog for delete
    var studentToDelete by remember { mutableStateOf<Student?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Student Directory",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    selectedStudent = null
                    showDialog = true
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add Student") },
                text = { Text("Add Student") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ── Error Banner ──────────────────────────────────────────────────
            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                ErrorBanner(
                    message = errorMessage ?: "",
                    onDismiss = viewModel::clearError
                )
            }

            // ── Search Bar ────────────────────────────────────────────────────
            SearchBar(
                query = searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // ── Loading indicator ─────────────────────────────────────────────
            if (isLoading && students.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // ── Empty state ───────────────────────────────────────────────────
            if (!isLoading && students.isEmpty()) {
                EmptyState(
                    hasSearchQuery = searchQuery.isNotBlank(),
                    modifier = Modifier.fillMaxSize()
                )
            }

            // ── Student list ──────────────────────────────────────────────────
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(students, key = { it.id }) { student ->
                    StudentCard(
                        student = student,
                        onEdit = {
                            selectedStudent = student
                            showDialog = true
                        },
                        onDelete = { studentToDelete = student }
                    )
                }
                // Bottom spacer so the FAB doesn't cover the last card
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    // ── Add / Edit Dialog ─────────────────────────────────────────────────────
    if (showDialog) {
        StudentFormDialog(
            student = selectedStudent,
            onDismiss = { showDialog = false },
            onConfirm = { student ->
                if (student.id.isBlank()) {
                    viewModel.addStudent(student)
                } else {
                    viewModel.updateStudent(student)
                }
                showDialog = false
            }
        )
    }

    // ── Delete Confirmation Dialog ────────────────────────────────────────────
    studentToDelete?.let { student ->
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            icon = { Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Delete Student") },
            text = { Text("Are you sure you want to delete \"${student.name}\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteStudent(student.id)
                        studentToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) { Text("Cancel") }
            }
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// StudentCard
// ──────────────────────────────────────────────────────────────────────────────

/**
 * A Material 3 card displaying summary information for a single [Student].
 *
 * Shows the student's name, ID, grade, GPA, and email.
 * Provides [onEdit] and [onDelete] icon-button actions.
 *
 * @param student  The student to display.
 * @param onEdit   Called when the user taps the edit icon.
 * @param onDelete Called when the user taps the delete icon.
 */
@Composable
fun StudentCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gpaBadgeColor = when {
        student.gpa >= 3.5 -> Color(0xFF2E7D32)   // Green – distinction
        student.gpa >= 2.5 -> Color(0xFF1565C0)   // Blue  – average
        else               -> Color(0xFFC62828)   // Red   – at risk
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar / GPA badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(gpaBadgeColor, gpaBadgeColor.copy(alpha = 0.6f))
                        )
                    )
            ) {
                Text(
                    text = student.name.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            // Student details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "ID: ${student.studentId}  ·  Grade: ${student.grade}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (student.email.isNotBlank()) {
                    Text(
                        text = student.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(4.dp))
                // GPA chip
                Surface(
                    shape = RoundedCornerShape(50),
                    color = gpaBadgeColor.copy(alpha = 0.12f),
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = "GPA ${String.format("%.2f", student.gpa)}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = gpaBadgeColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Actions
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit ${student.name}",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete ${student.name}",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// StudentFormDialog – Add / Edit
// ──────────────────────────────────────────────────────────────────────────────

/**
 * A modal dialog with a form for creating or editing a student record.
 *
 * When [student] is null the dialog operates in **Add** mode; otherwise it
 * pre-fills the form fields with the given student's data for **Edit** mode.
 *
 * @param student   The student to edit, or null to create a new one.
 * @param onDismiss Called when the user dismisses the dialog without saving.
 * @param onConfirm Called with the final [Student] when the user taps Save.
 */
@Composable
fun StudentFormDialog(
    student: Student?,
    onDismiss: () -> Unit,
    onConfirm: (Student) -> Unit
) {
    val isEditMode = student != null

    // Form state – pre-fill from existing student or leave blank.
    var name      by remember { mutableStateOf(student?.name      ?: "") }
    var studentId by remember { mutableStateOf(student?.studentId ?: "") }
    var email     by remember { mutableStateOf(student?.email     ?: "") }
    var phone     by remember { mutableStateOf(student?.phone     ?: "") }
    var grade     by remember { mutableStateOf(student?.grade     ?: "") }
    var gpa       by remember { mutableStateOf(student?.gpa?.toString() ?: "") }
    var address   by remember { mutableStateOf(student?.address   ?: "") }

    var nameError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditMode) "Edit Student" else "Add New Student") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
            ) {
                // Name (required)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = { Text("Full Name *") },
                    isError = nameError,
                    supportingText = if (nameError) ({ Text("Name is required") }) else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Student ID
                OutlinedTextField(
                    value = studentId,
                    onValueChange = { studentId = it },
                    label = { Text("Student ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Grade
                OutlinedTextField(
                    value = grade,
                    onValueChange = { grade = it },
                    label = { Text("Grade / Class") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // GPA
                OutlinedTextField(
                    value = gpa,
                    onValueChange = { gpa = it },
                    label = { Text("GPA (0.0 – 4.0)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Address
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isBlank()) {
                    nameError = true
                    return@Button
                }
                val finalStudent = (student ?: Student.empty()).copy(
                    name      = name.trim(),
                    studentId = studentId.trim(),
                    email     = email.trim(),
                    phone     = phone.trim(),
                    grade     = grade.trim(),
                    gpa       = gpa.toDoubleOrNull() ?: 0.0,
                    address   = address.trim()
                )
                onConfirm(finalStudent)
            }) {
                Text(if (isEditMode) "Update" else "Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// Helper composables
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search by name, ID, or grade…") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50)
    )
}

@Composable
private fun ErrorBanner(message: String, onDismiss: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "Dismiss error",
                    modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun EmptyState(hasSearchQuery: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (hasSearchQuery) Icons.Filled.Search else Icons.Filled.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = if (hasSearchQuery) "No students match your search" else "No students yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (hasSearchQuery) "Try a different name or ID" else "Tap + to add the first student",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
