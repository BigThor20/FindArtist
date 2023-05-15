package com.example.findartist.model

import androidx.activity.viewModels
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

class SelectJobViewModel : ViewModel() {

    private val db: FirebaseFirestore = Firebase.firestore

    fun fetchFirstSpinnerData(): LiveData<List<String>> {
        val firstSpinnerData = MutableLiveData<List<String>>()

        viewModelScope.launch {
            val data = getFirstSpinnerData()
            firstSpinnerData.value = data
        }

        return firstSpinnerData
    }

    private suspend fun getFirstSpinnerData(): List<String> {
        return withContext(Dispatchers.IO) {
            val documents = db.collection("industries").get().await()
            documents.map { it["value"].toString() }
        }
    }

    fun fetchSecondSpinnerData(selectedOption: String): LiveData<List<String>> {
        val secondSpinnerData = MutableLiveData<List<String>>()

        viewModelScope.launch {
            val data = getSecondSpinnerData(selectedOption)
            secondSpinnerData.value = data
        }

        return secondSpinnerData
    }

    private suspend fun getSecondSpinnerData(selectedOption: String): List<String> {
        return withContext(Dispatchers.IO) {
            val document = db.collection("jobs").document(selectedOption).get().await()
            document["options"] as List<String>
        }
    }
}