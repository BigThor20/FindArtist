package com.example.findartist.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findartist.model.ArtistItemList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ArtistCardViewModel : ViewModel() {

    private val _artistList = MutableLiveData<List<ArtistItemList>>()
    val artistList: LiveData<List<ArtistItemList>> = _artistList

    fun fetchArtistsFromFirestore(industry : String?, job : String?, location : String?, name : String?) {
        viewModelScope.launch {
            try {
                val fetchedArtists = withContext(Dispatchers.IO) {

                    val documents = getArtistDocuments(industry, job, location, name)

                    val artistList = mutableListOf<ArtistItemList>()
                    for (document in documents) {
                        val artistItem = document.toObject(ArtistItemList::class.java)
                        val rating = getAvgRating(document.id)
                        val updatedArtistItem = artistItem.copy(id = document.id, rate = rating)
                        artistList.add(updatedArtistItem)
                        Log.i("artist", updatedArtistItem.toString())
                    }
                    artistList
                }

                _artistList.value = fetchedArtists
            } catch (exception: Exception) {
                // Handle the error
            }
        }
    }

    suspend fun getArtistDocuments(industry : String?, job : String?, city : String?, name : String?): QuerySnapshot {
        val db = FirebaseFirestore.getInstance()

        var query = db.collection("users").whereEqualTo("role", "ARTIST")
        if (!industry.isNullOrEmpty()) {
            query = query.whereEqualTo("industry", industry)
        }
        if (!job.isNullOrEmpty()) {
            query = query.whereEqualTo("job", job)
        }
        if (!city.isNullOrEmpty()) {
            query = query.whereEqualTo("city", city)
        }
        if (!name.isNullOrEmpty()) {
            query = query.whereEqualTo("firstName", name)
        }

        return query.get().await()

    }

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
