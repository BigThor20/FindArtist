package com.example.findartist.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture

class ProfileViewModel : ViewModel() {
    fun addRating(userId: String, artistId: String?, rating: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val db = FirebaseFirestore.getInstance()

                val userDocRef = db.collection("users").document(artistId.toString())

                userDocRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val ratingsList = documentSnapshot.get("ratings") as? ArrayList<Map<String, String>>
                        var ratingExist = false
                        if (ratingsList != null) {
                            // Process the ratings list
                            for (rating in ratingsList) {
                                val id = rating["userId"] as? String
                                if (userId == id) {
                                    ratingExist = true
                                }
                            }
                        }

                        if (!ratingExist) {
                            val ratingMap = mapOf<String, String>(
                                "userId" to userId.toString(),
                                "rate" to rating.toString()
                            )
                            userDocRef.update("ratings", FieldValue.arrayUnion(ratingMap))
                            Log.i("FirestoreDB", "Rating added successfully")
                        } else {
                            Log.i("FirestoreDB", "Rating already exists for this user")
                        }
                    } else {
                        Log.d("Firestore", "Document does not exist")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("Firestore", "Error getting document: ", exception)
                }
            } catch (exception: Exception) {
                Log.e("FirestoreDB", "Error adding rating: ", exception)
            }
        }
    }



    fun removeRating(userId: String, artistId : String?) {}
    suspend fun getAvgRating(myId: String): Float = withContext(Dispatchers.IO) {
        val firestore = FirebaseFirestore.getInstance()
        val documentRef = firestore.collection("users").document(myId)

        val snapshot = documentRef.get().await()

        if (snapshot.exists()) {
            val ratings = snapshot.get("ratings") as List<HashMap<String, Any>>

            var sum = 0f
            for (rating in ratings) {
                val rate = rating["rate"] as String
                sum += rate.toFloatOrNull() ?: 0f
            }

            if (sum != 0f) {
                return@withContext sum / ratings.size
            }
        }

        return@withContext 0f
    }


}