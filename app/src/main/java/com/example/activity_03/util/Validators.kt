package com.example.activity_03.util

/**
 * Validation utilities for student registration input fields.
 */
object Validators {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    private val PHONE_REGEX = "^[+]?[0-9\\s-]{7,15}$".toRegex()

    fun validateName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult.Error("Name cannot be empty")
            trimmed.length < 2 -> ValidationResult.Error("Name must be at least 2 characters")
            trimmed.length > 100 -> ValidationResult.Error("Name cannot exceed 100 characters")
            else -> ValidationResult.Success
        }
    }

    fun validateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return ValidationResult.Success // Optional field
        return if (EMAIL_REGEX.matches(trimmed)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Please enter a valid email address")
        }
    }

    fun validatePhone(phone: String): ValidationResult {
        val trimmed = phone.trim()
        if (trimmed.isEmpty()) return ValidationResult.Success // Optional field
        return if (PHONE_REGEX.matches(trimmed)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Please enter a valid phone number")
        }
    }

    fun validateGpa(gpaStr: String): ValidationResult {
        val trimmed = gpaStr.trim()
        if (trimmed.isEmpty()) return ValidationResult.Success // Defaults to 0.0
        val gpa = trimmed.toDoubleOrNull()
            ?: return ValidationResult.Error("GPA must be a valid number")
        return if (gpa in 0.0..4.0) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("GPA must be between 0.0 and 4.0")
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()

    val isValid: Boolean get() = this is Success
}
