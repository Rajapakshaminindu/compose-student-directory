package com.example.activity_03.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.activity_03.model.Student
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modal dialog displaying detailed information about a student including
 * contact details, academic grade, registration timestamp, and address.
 */
@Composable
fun StudentDetailDialog(
    student: Student,
    onDismiss: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(student.name, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailRow(icon = Icons.Filled.Badge, label = "Student ID", value = student.studentId.ifBlank { "N/A" })
                DetailRow(icon = Icons.Filled.School, label = "Grade / Class", value = student.grade.ifBlank { "N/A" })
                DetailRow(icon = Icons.Filled.Star, label = "GPA", value = String.format("%.2f", student.gpa))
                DetailRow(icon = Icons.Filled.Email, label = "Email", value = student.email.ifBlank { "N/A" })
                DetailRow(icon = Icons.Filled.Phone, label = "Phone", value = student.phone.ifBlank { "N/A" })
                DetailRow(icon = Icons.Filled.Home, label = "Address", value = student.address.ifBlank { "N/A" })

                Divider(Modifier.padding(vertical = 4.dp))

                DetailRow(
                    icon = Icons.Filled.CalendarToday,
                    label = "Enrolled",
                    value = dateFormat.format(Date(student.createdAt))
                )
                DetailRow(
                    icon = Icons.Filled.Update,
                    label = "Last Updated",
                    value = dateFormat.format(Date(student.updatedAt))
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
