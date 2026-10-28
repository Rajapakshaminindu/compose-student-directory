package com.example.activity_03.util

import com.example.activity_03.model.Student

/**
 * Utility for exporting Student records to comma-separated values (CSV) format.
 */
object CsvExportUtil {

    private const val CSV_HEADER = "DocumentId,Name,StudentID,Email,Phone,Grade,GPA,Address,CreatedAt,UpdatedAt"

    /**
     * Converts a list of [Student] records into a well-formatted CSV string.
     * Escapes fields containing commas or quotes according to RFC 4180.
     */
    fun exportToCsv(students: List<Student>): String {
        val builder = StringBuilder()
        builder.append(CSV_HEADER).append("\n")

        for (student in students) {
            val line = listOf(
                escapeCsv(student.id),
                escapeCsv(student.name),
                escapeCsv(student.studentId),
                escapeCsv(student.email),
                escapeCsv(student.phone),
                escapeCsv(student.grade),
                String.format("%.2f", student.gpa),
                escapeCsv(student.address),
                student.createdAt.toString(),
                student.updatedAt.toString()
            ).joinToString(",")

            builder.append(line).append("\n")
        }

        return builder.toString()
    }

    private fun escapeCsv(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}
