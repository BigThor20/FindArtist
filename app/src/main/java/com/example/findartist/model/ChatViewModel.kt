package com.example.findartist.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChatViewModel  : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore

    fun getReceiverName(
        id : String,
        callback: (String?) -> Unit
        ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val userDocRef = db.collection("users").document(id)
                val document = userDocRef.get().await()
                if (document != null && document.exists()) {
                    val firstName = document.getString("firstName")
                    val lastName = document.getString("lastName")
                    val fieldValue = "$firstName $lastName"
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

    fun addMessageToFirestore(sender : String, receiver : String, message : String) {
        val messageData = HashMap<String, String>()
        messageData["sender"] = sender
        messageData["content"] = message
        messageData["date"] = getCurrentDate()

        val chatRef = db.collection("chats").document("$sender-$receiver")

        // Rulează operația într-un context non-blocking folosind corutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot = chatRef.get().await()
                if (!documentSnapshot.exists()) {
                    // Documentul nu există, setează field-urile "sender" și "receiver"
                    val initialData = HashMap<String, Any>()
                    initialData["sender"] = sender
                    initialData["receiver"] = receiver
                    initialData["messages"] = listOf(messageData)

                    chatRef.set(initialData).await()
                } else {
                    // Documentul există deja, adaugă mesajul în array-ul "messages"
                    chatRef.update("messages", FieldValue.arrayUnion(messageData)).await()
                }
                // Mesajul a fost adăugat cu succes în Firestore
            } catch (e: Exception) {
                // Eroare la adăugarea mesajului în Firestore
                // Gestionează eroarea aici
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }




}

