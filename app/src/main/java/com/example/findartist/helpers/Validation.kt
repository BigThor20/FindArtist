package com.example.findartist.helpers

import java.util.regex.Pattern

class Validation {
    fun isEmailValid(email: String): ValidationResult {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return if (matcher.matches()) ValidationResult.Success else ValidationResult.Error("Invalid email format")
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): ValidationResult {
        return if (password == confirmPassword) ValidationResult.Success else ValidationResult.Error("Passwords do not match")
    }

    fun isPhoneNumberValid(phone: String): ValidationResult {
        val phonePattern = "^07[0-9]{8}$"
        val pattern = Pattern.compile(phonePattern)
        val matcher = pattern.matcher(phone)
        return if (matcher.matches()) ValidationResult.Success else ValidationResult.Error("Invalid phone number")
    }

    fun isNameValid(name: String): ValidationResult {
        val namePattern = "^[a-zA-Z]+(\\s+[a-zA-Z]+)?$"
        val pattern = Pattern.compile(namePattern)
        val matcher = pattern.matcher(name)
        return if (matcher.matches()) ValidationResult.Success else ValidationResult.Error("Invalid name format")
    }
}