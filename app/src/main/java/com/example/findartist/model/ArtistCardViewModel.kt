package com.example.findartist.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findartist.model.ArtistItemList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ArtistCardViewModel : ViewModel() {

    private val _artistList = MutableLiveData<List<ArtistItemList>>()
    val artistList: LiveData<List<ArtistItemList>> = _artistList

    fun fetchArtistsFromFirestore() {
        viewModelScope.launch {
            try {
                val fetchedArtists = withContext(Dispatchers.IO) {
                    val db = FirebaseFirestore.getInstance()
                    val documents = db.collection("users")
                        .whereEqualTo("role", "ARTIST")
                        .get()
                        .await()

                    val artistList = mutableListOf<ArtistItemList>()
                    for (document in documents) {
                        val artistItem = document.toObject(ArtistItemList::class.java)
                        artistList.add(artistItem)
                    }
                    artistList
                }

                _artistList.value = fetchedArtists
            } catch (exception: Exception) {
                // Handle the error
            }
        }
    }
}
