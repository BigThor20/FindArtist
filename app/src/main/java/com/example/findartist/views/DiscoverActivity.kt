package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.model.ArtistCardViewModel

class DiscoverActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val viewModel: ArtistCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_page)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        viewModel.fetchArtistsFromFirestore()

        viewModel.artistList.observe(this, Observer { artistList ->
            val adapter = ItemAdapter(this, artistList)
            val intent = Intent(this, ProfileActivity::class.java)
            recyclerView.adapter = adapter

            adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    startActivity(intent)
                }
            })
        })
    }
}
