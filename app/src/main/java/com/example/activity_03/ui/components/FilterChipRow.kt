package com.example.activity_03.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Horizontally scrollable row of Material 3 FilterChips for rapid filtering
 * of students by academic grade or category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipRow(
    availableGrades: List<String>,
    selectedGrade: String?,
    onGradeSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" chip
        FilterChip(
            selected = selectedGrade == null,
            onClick = { onGradeSelected(null) },
            label = { Text("All") }
        )

        availableGrades.forEach { grade ->
            FilterChip(
                selected = selectedGrade == grade,
                onClick = {
                    onGradeSelected(if (selectedGrade == grade) null else grade)
                },
                label = { Text(grade) }
            )
        }
    }
}
