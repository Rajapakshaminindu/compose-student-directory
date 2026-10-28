package com.example.activity_03.model

import com.google.firebase.firestore.DocumentId

/**
 * Data model representing a Student entity.
 *
 * This data class maps directly to Firestore documents in the "students" collection.
 * All fields have default values so Firestore can deserialize documents automatically.
 *
 * @property id         Firestore document ID (auto-assigned via @DocumentId)
 * @property name       Full name of the student
 * @property studentId  Unique student registration/roll number
 * @property email      Student's email address
 * @property phone      Student's contact phone number
 * @property grade      Academic grade or class (e.g., "10th", "Grade A")
 * @property gpa        Grade Point Average (0.0 – 4.0 scale)
 * @property address    Home address of the student
 * @property createdAt  Unix timestamp (ms) when the record was first created
 * @property updatedAt  Unix timestamp (ms) when the record was last updated
 */
data class Student(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val studentId: String = "",
    val email: String = "",
    val phone: String = "",
    val grade: String = "",
    val gpa: Double = 0.0,
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Converts a Student instance to a plain Map for Firestore write operations.
     * The [id] field is intentionally excluded because Firestore manages it separately
     * via the document path / @DocumentId annotation.
     */
    fun toMap(): Map<String, Any> = mapOf(
        "name"       to name,
        "studentId"  to studentId,
        "email"      to email,
        "phone"      to phone,
        "grade"      to grade,
        "gpa"        to gpa,
        "address"    to address,
        "createdAt"  to createdAt,
        "updatedAt"  to updatedAt
    )

    /**
     * Returns a copy of this Student with [updatedAt] refreshed to the current time.
     * Use this helper before pushing an edit back to Firestore.
     */
    fun withUpdatedTimestamp(): Student = copy(updatedAt = System.currentTimeMillis())

    /**
     * Simple display helper – returns a formatted label shown in list items.
     */
    fun displayLabel(): String = "$name  ·  $grade"

    companion object {
        /** Firestore collection name used by the whole app. */
        const val COLLECTION = "students"

        /**
         * Factory function that creates an *empty* Student template ready for
         * the "Add Student" dialog, saving the caller from spelling out every default.
         */
        fun empty(): Student = Student()
    }
}
