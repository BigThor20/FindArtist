package com.example.findartist.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findartist.helpers.ValidationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    private val _validationError = MutableLiveData<String>()
    val validationError: LiveData<String> = _validationError


    fun register(user: User, password: String, confirmPassword: String) {
        viewModelScope.launch {
            val emailError = isEmailValid(user.email)
            val passwordError = doPasswordsMatch(password, confirmPassword)
            val firstNameError = isNameValid(user.firstName)
            val lastNameError = isNameValid(user.lastName)

            if (emailError is ValidationResult.Success &&
                passwordError is ValidationResult.Success &&
                firstNameError is ValidationResult.Success &&
                lastNameError is ValidationResult.Success) {
                try {
                    val result = withContext(Dispatchers.IO) {
                        auth.createUserWithEmailAndPassword(user.email, password).await()
                    }
                    if (result.user != null) {
                        saveUserToFirestore(user, result.user!!.uid)
                        _registerStatus.postValue(true)
                    } else {
                        _registerStatus.postValue(false)
                    }
                } catch (e: Exception) {
                    // Tratarea erorii
                    _registerStatus.postValue(false)
                }
            } else {
                val errorMessage = listOf(emailError, passwordError,
                    firstNameError, lastNameError)
                    .filterIsInstance<ValidationResult.Error>()
                    .joinToString("\n") { it.message }
                _validationError.postValue(errorMessage)
            }

        }
    }

    private suspend fun saveUserToFirestore(user: User, userId: String) = withContext(Dispatchers.IO) {
        val userMap = hashMapOf(
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "role" to user.role,
            "email" to user.email,
            "city" to user.city
        )
        db.collection("users").document(userId).set(userMap).await()
    }

    private fun isEmailValid(email: String): ValidationResult {
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return if (matcher.matches()) ValidationResult.Success else ValidationResult.Error("Invalid email format")
    }

    private fun doPasswordsMatch(password: String, confirmPassword: String): ValidationResult {
        return if (password == confirmPassword) ValidationResult.Success else ValidationResult.Error("Passwords do not match")
    }

    private fun isPhoneNumberValid(phone: String): ValidationResult {
        val phonePattern = "^07[0-9]{8}$"
        val pattern = Pattern.compile(phonePattern)
        val matcher = pattern.matcher(phone)
        return if (matcher.matches()) ValidationResult.Success else ValidationResult.Error("Invalid phone number")
    }

    private fun isNameValid(name: String): ValidationResult {
        val namePattern = "^[a-zA-Z]+(\\s+[a-zA-Z]+)?$"
        val pattern = Pattern.compile(namePattern)
        val matcher = pattern.matcher(name)
        return if (matcher.matches()) ValidationResult.Success else ValidationResult.Error("Invalid name format")
    }
}