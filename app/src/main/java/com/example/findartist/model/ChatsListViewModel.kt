package com.example.findartist.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ChatsListViewModel : ViewModel() {
    private val _chatItems = MutableLiveData<List<ChatItemList>>()
    val chatItems: LiveData<List<ChatItemList>> get() = _chatItems

    fun getReceiversForSender(sender: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                val firestore = FirebaseFirestore.getInstance()
                val collectionRef = firestore.collection("chats")

                val query = collectionRef.whereEqualTo("sender", sender)
                val snapshot = query.get().await()

                val receivers = mutableListOf<String>()

                for (document in snapshot.documents) {
                    val receiver = document.getString("receiver")
                    if (receiver != null) {
                        receivers.add(receiver)
                    }
                }
                val users = firestore.collection("users")

                val chatItems = mutableListOf<ChatItemList>()
                for (receiver in receivers) {
                    val documentRef = users.document(receiver)
                    val documentSnapshot = documentRef.get().await()

                    if (documentSnapshot.exists()) {
                        val firstName = documentSnapshot.getString("firstName") ?: ""
                        val lastName = documentSnapshot.getString("lastName") ?: ""
                        val name = "$firstName $lastName"
                        val profilePhotoUrl = documentSnapshot.getString("profilePhotoUrl") ?: ""
                        val artistId = documentSnapshot.id

                        val chatItem = ChatItemList(name, profilePhotoUrl, artistId)
                        chatItems.add(chatItem)
                    }
                }

                chatItems
            }

            _chatItems.value = result ?: emptyList()
        }
    }




}