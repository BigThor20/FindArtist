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
import java.text.FieldPosition

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

    suspend fun updateFieldValueFromCollection(
        collectionName: String,
        fieldName: String,
        userId: String,
        value: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocRef = firestore.collection(collectionName).document(userId)
                val updateData = hashMapOf<String, Any>(fieldName to value)
                userDocRef.update(updateData).await()
                Log.i("FirestoreDB", "field $fieldName updated")
            } catch (exception: Exception) {
                Log.d("FireStore", "Error updating field value: ", exception)
            }
        }
    }

    fun getArrayElementFromCollection(
        collectionName: String,
        fieldName: String,
        userId: String,
        position: Int,
        callback: (String?) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocRef = firestore.collection(collectionName).document(userId)
                val document = userDocRef.get().await()
                if (document != null && document.exists()) {
                    val fieldValue = document[fieldName] as? ArrayList<*>
                    val element = fieldValue?.getOrNull(position) as? String
                    launch(Dispatchers.Main) {
                        callback(element)
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

    suspend fun addElementToAnArrayFromCollection(
        collectionName: String,
        fieldName: String,
        userId: String,
        value: String,
        position: Int
    ) {
        withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocRef = firestore.collection(collectionName).document(userId)

                // Get the current array from the document
                val documentSnapshot = userDocRef.get().await()
                val currentArray = documentSnapshot[fieldName] as? ArrayList<String> ?: arrayListOf()

                // Check if position is within the valid range
                if (position >= 0 && position < currentArray.size) {
                    // Replace the element at the specified position with the new value
                    currentArray[position] = value
                } else  {
                    // Add the new value at the end of the list
                    currentArray.add(value)
                }

                // Update the field with the modified array
                val updateData = hashMapOf<String, Any>(fieldName to currentArray)
                userDocRef.update(updateData).await()

                Log.i("FirestoreDB", "Field $fieldName updated: element at position $position replaced")
            } catch (exception: Exception) {
                Log.d("FirestoreDB", "Error updating field value: ", exception)
            }
        }
    }


    suspend fun deleteElementFromAnArrayFromCollection(
        collectionName: String,
        fieldName: String,
        userId: String,
        position: Int
    ) {
        withContext(Dispatchers.IO) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocRef = firestore.collection(collectionName).document(userId)

                // Get the current array from the document
                val documentSnapshot = userDocRef.get().await()
                val currentArray = documentSnapshot[fieldName] as? ArrayList<String> ?: arrayListOf()

                // Check if position is within the valid range
                if (position >= 0 && position < currentArray.size) {
                    // Remove the element at the specified position
                    currentArray.removeAt(position)

                    // Update the field with the modified array
                    val updateData = hashMapOf<String, Any>(fieldName to currentArray)
                    userDocRef.update(updateData).await()

                    Log.i("FirestoreDB", "Field $fieldName updated: element at position $position deleted")
                } else {
                    Log.e("FirestoreDB", "Invalid position: $position")
                }
            } catch (exception: Exception) {
                Log.d("FirestoreDB", "Error updating field value: ", exception)
            }
        }
    }



}



