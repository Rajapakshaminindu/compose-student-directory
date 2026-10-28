package com.example.activity_03.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorsTest {

    @Test
    fun testNameValidation() {
        assertFalse(Validators.validateName("").isValid)
        assertFalse(Validators.validateName("A").isValid)
        assertTrue(Validators.validateName("John Doe").isValid)
    }

    @Test
    fun testEmailValidation() {
        assertTrue(Validators.validateEmail("").isValid) // optional
        assertTrue(Validators.validateEmail("student@university.edu").isValid)
        assertFalse(Validators.validateEmail("not-an-email").isValid)
        assertFalse(Validators.validateEmail("missing@domain").isValid)
    }

    @Test
    fun testGpaValidation() {
        assertTrue(Validators.validateGpa("").isValid)
        assertTrue(Validators.validateGpa("3.75").isValid)
        assertTrue(Validators.validateGpa("0.0").isValid)
        assertTrue(Validators.validateGpa("4.0").isValid)
        assertFalse(Validators.validateGpa("4.5").isValid)
        assertFalse(Validators.validateGpa("-1.0").isValid)
        assertFalse(Validators.validateGpa("abc").isValid)
    }
}
