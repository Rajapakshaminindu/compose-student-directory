package com.example.activity_03.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StudentTest {

    @Test
    fun testEmptyStudentCreation() {
        val student = Student.empty()
        assertEquals("", student.id)
        assertEquals("", student.name)
        assertEquals(0.0, student.gpa, 0.001)
    }

    @Test
    fun testToMapConversion() {
        val student = Student(
            id = "doc_123",
            name = "Jane Doe",
            studentId = "ST-9988",
            email = "jane@example.com",
            phone = "+1234567890",
            grade = "A+",
            gpa = 3.9,
            address = "123 Academic Way"
        )

        val map = student.toMap()

        assertEquals("Jane Doe", map["name"])
        assertEquals("ST-9988", map["studentId"])
        assertEquals("jane@example.com", map["email"])
        assertEquals("A+", map["grade"])
        assertEquals(3.9, map["gpa"])
        // Ensure id is not inside the document fields map
        assertTrue(!map.containsKey("id"))
    }

    @Test
    fun testWithUpdatedTimestamp() {
        val student = Student(
            id = "doc_1",
            name = "John Smith",
            updatedAt = 1000L
        )

        val updated = student.withUpdatedTimestamp()
        assertNotEquals(1000L, updated.updatedAt)
        assertTrue(updated.updatedAt > 1000L)
    }

    @Test
    fun testDisplayLabel() {
        val student = Student(name = "Alice", grade = "12th Grade")
        assertEquals("Alice  ·  12th Grade", student.displayLabel())
    }
}
