package com.example.findartist

import com.example.findartist.helpers.Validation
import com.example.findartist.helpers.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidationTest {

    private lateinit var validation: Validation

    @Before
    fun setup() {
        validation = Validation()
    }

    @Test
    fun testIsEmailValid_ValidEmail_ReturnsSuccess() {
        // Given
        val email = "test@example.com"

        // When
        val result = validation.isEmailValid(email)

        // Then
        assertEquals(ValidationResult.Success, result)
    }

    @Test
    fun testIsEmailValid_InvalidEmail_ReturnsError() {
        // Given
        val email = "invalid-email"

        // When
        val result = validation.isEmailValid(email)

        // Then
        assertEquals(ValidationResult.Error("Invalid email format"), result)
    }

    @Test
    fun testDoPasswordsMatch_PasswordsMatch_ReturnsSuccess() {
        // Given
        val password = "password"
        val confirmPassword = "password"

        // When
        val result = validation.doPasswordsMatch(password, confirmPassword)

        // Then
        assertEquals(ValidationResult.Success, result)
    }

    @Test
    fun testDoPasswordsMatch_PasswordsDoNotMatch_ReturnsError() {
        // Given
        val password = "password"
        val confirmPassword = "different-password"

        // When
        val result = validation.doPasswordsMatch(password, confirmPassword)

        // Then
        assertEquals(ValidationResult.Error("Passwords do not match"), result)
    }

    @Test
    fun testIsNameValid_ValidName_ReturnsSuccess() {
        // Given
        val name = "John Doe"

        // When
        val result = validation.isNameValid(name)

        // Then
        assertEquals(ValidationResult.Success, result)
    }

    @Test
    fun testIsNameValid_InvalidName_ReturnsError() {
        // Given
        val name = "12345"

        // When
        val result = validation.isNameValid(name)

        // Then
        assertEquals(ValidationResult.Error("Invalid name format"), result)
    }
}
