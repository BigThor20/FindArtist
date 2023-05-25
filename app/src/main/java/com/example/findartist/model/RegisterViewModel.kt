package com.example.findartist.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findartist.helpers.Validation
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
            val validation = Validation();
            val emailError = validation.isEmailValid(user.email)
            val passwordError = validation.doPasswordsMatch(password, confirmPassword)
            val firstNameError = validation.isNameValid(user.firstName)
            val lastNameError = validation.isNameValid(user.lastName)

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

    suspend fun saveUserToFirestore(user: User, userId: String) = withContext(Dispatchers.IO) {
        val userMap = user.toMap()
        db.collection("users").document(userId).set(userMap).await()
    }



}