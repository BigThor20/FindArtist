package com.example.findartist.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    private val _registerStatus = MutableLiveData<Boolean>()
    val registerStatus: LiveData<Boolean> = _registerStatus

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(email, password).await()
                }
                if (result.user != null) {
                    val userData = hashMapOf("email" to email)
                    db.collection("users").document(result.user!!.uid).set(userData).await()
                    // Înregistrarea a avut succes
                    _registerStatus.postValue(true)
                } else {
                    _registerStatus.postValue(false)
                }
            } catch (e: Exception) {
                // Tratarea erorii
                _registerStatus.postValue(false)
            }
        }
    }
}