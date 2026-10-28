package com.example.activity_03.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AcademicRecordTest {

    @Test
    fun testAcademicRecordSerialization() {
        val record = AcademicRecord(
            id = "rec_001",
            studentId = "ST-101",
            courseCode = "CS101",
            courseName = "Introduction to Computer Science",
            credits = 4,
            score = 92.5,
            letterGrade = "A",
            semester = "Spring 2026"
        )

        val map = record.toMap()
        assertEquals("ST-101", map["studentId"])
        assertEquals("CS101", map["courseCode"])
        assertEquals(4, map["credits"])
        assertEquals(92.5, map["score"])
        assertEquals("A", map["letterGrade"])
    }

    @Test
    fun testAttendanceSerialization() {
        val attendance = Attendance(
            id = "att_001",
            studentId = "ST-101",
            status = AttendanceStatus.PRESENT,
            remarks = "On time"
        )

        val map = attendance.toMap()
        assertEquals("ST-101", map["studentId"])
        assertEquals("PRESENT", map["status"])
        assertEquals("On time", map["remarks"])
        assertNotNull(map["date"])
    }
}
