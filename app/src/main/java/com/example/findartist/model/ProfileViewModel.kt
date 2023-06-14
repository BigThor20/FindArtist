package com.example.findartist.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

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

    private fun checkIfRatingExistFromUser(userId: String, artistId: String?) : Boolean {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("users").document(artistId.toString())
        var returnVal = false
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val ratingsList = documentSnapshot.get("ratings") as? ArrayList<Map<String, String>>
                if (ratingsList != null) {
                    // Process the ratings list
                    for (rating in ratingsList) {
                        val id = rating["userId"] as? String
                        if (userId == id) {
                            returnVal = true
                        }
                    }
                    Log.i("PLM1", returnVal.toString())
                }
            } else {
                Log.d("Firestore", "Document does not exist")
            }
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error getting document: ", exception)
        }

        Log.i("PLM2", returnVal.toString())
        return returnVal

    }

    fun removeRating(userId: String, artistId : String?) {}
    fun getAvgRating(artistId: String) {}
}