package com.example.activity_03.model

import com.google.firebase.firestore.DocumentId

/**
 * Data model representing a student's academic course score and semester grade.
 */
data class AcademicRecord(
    @DocumentId
    val id: String = "",
    val studentId: String = "",
    val courseCode: String = "",
    val courseName: String = "",
    val credits: Int = 3,
    val score: Double = 0.0,
    val letterGrade: String = "A",
    val semester: String = "Fall 2026"
) {
    fun toMap(): Map<String, Any> = mapOf(
        "studentId" to studentId,
        "courseCode" to courseCode,
        "courseName" to courseName,
        "credits" to credits,
        "score" to score,
        "letterGrade" to letterGrade,
        "semester" to semester
    )
}

/**
 * Data model for recording daily or session attendance of a student.
 */
data class Attendance(
    @DocumentId
    val id: String = "",
    val studentId: String = "",
    val date: Long = System.currentTimeMillis(),
    val status: AttendanceStatus = AttendanceStatus.PRESENT,
    val remarks: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "studentId" to studentId,
        "date" to date,
        "status" to status.name,
        "remarks" to remarks
    )
}

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    EXCUSED
}
