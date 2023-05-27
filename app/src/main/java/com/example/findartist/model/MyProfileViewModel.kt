package com.example.findartist.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyProfileViewModel : ViewModel() {

    fun getFieldValueFromCollection(
        collectionName: String,
        fieldName: String,
        userId: String,
        callback: (String?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocRef = firestore.collection(collectionName).document(userId)
                val document = userDocRef.get().await()
                if (document != null && document.exists()) {
                    val fieldValue = document.getString(fieldName)
                    launch(Dispatchers.Main) {
                        callback(fieldValue)
                    }
                } else {
                    Log.d("FireStore", "No such document")
                    launch(Dispatchers.Main) {
                        callback(null)
                    }
                }
            } catch (exception: Exception) {
                Log.d("FireStore", "Error getting document: ", exception)
                launch(Dispatchers.Main) {
                    callback(null)
                }
            }
        }
    }


}