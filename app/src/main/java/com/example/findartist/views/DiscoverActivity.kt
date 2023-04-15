package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.model.ArtistItemList
import com.google.firebase.firestore.FirebaseFirestore

class DiscoverActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_page)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        fetchArtistsFromFirestore()
    }

    private fun fetchArtistsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("role", "ARTIST")
            .get()
            .addOnSuccessListener { documents ->
                val artistList = mutableListOf<ArtistItemList>()
                for (document in documents) {
                    val artistItem = document.toObject(ArtistItemList::class.java)
                    artistList.add(artistItem)
                }

                val adapter = ItemAdapter(this, artistList)
                val intent = Intent(this, ProfileActivity::class.java)
                recyclerView.adapter = adapter

                adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        startActivity(intent)
                    }
                })

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    companion object {
        private const val TAG = "DiscoverActivity"
    }

}
