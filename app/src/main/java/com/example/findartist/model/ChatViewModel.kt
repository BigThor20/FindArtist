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
    private val _messageItems = MutableLiveData<List<MessageItem>>()
    val messageItems: LiveData<List<MessageItem>> get() = _messageItems

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

    fun addMessageToFirestore(sender: String, receiver: String, message: String) {
        val messageData = HashMap<String, String>()
        messageData["sender"] = sender
        messageData["content"] = message
        messageData["date"] = getCurrentDate()

        val chatRef1 = db.collection("chats").document("$sender-$receiver")
        val chatRef2 = db.collection("chats").document("$receiver-$sender")

        // Rulează operația într-un context non-blocking folosind corutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val documentSnapshot1 = chatRef1.get().await()
                val documentSnapshot2 = chatRef2.get().await()

                if (!documentSnapshot1.exists() && !documentSnapshot2.exists()) {
                    // Niciunul dintre documente nu există, creează unul nou cu id-ul "$sender-$receiver"
                    val initialData = HashMap<String, Any>()
                    initialData["sender"] = sender
                    initialData["receiver"] = receiver
                    initialData["messages"] = listOf(messageData)

                    chatRef1.set(initialData).await()
                } else {
                    // Unul dintre documente există, adaugă mesajul în documentul corespunzător
                    val chatRef = if (documentSnapshot1.exists()) chatRef1 else chatRef2
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

    fun getMessagesFromFirestore(documentId: String, documentId2: String) {
        viewModelScope.launch {
            val messages = withContext(Dispatchers.IO) {
                val documentRef = db.collection("chats").document(documentId)
                val documentSnapshot = documentRef.get().await()

                val messageList = if (documentSnapshot.exists()) {
                    documentSnapshot.toObject(ChatDocument::class.java)?.messages
                } else {
                    val documentRef2 = db.collection("chats").document(documentId2)
                    val documentSnapshot2 = documentRef2.get().await()
                    documentSnapshot2.toObject(ChatDocument::class.java)?.messages
                }

                messageList ?: emptyList()
            }

            val chatItems = messages.map { message ->
                MessageItem(message.content, message.sender, message.date)
            }

            _messageItems.value = chatItems
        }
    }

}
data class ChatDocument(
    val messages: List<MessageItem> = emptyList()
)

