package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.data.Datasource

class DiscoverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_page)

        // Initialize data
        val myDataset = Datasource().loadArtistsList();

        val adapter = ItemAdapter(this, myDataset)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val intent = Intent(this, ProfileActivity::class.java)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
//                Toast.makeText(this@DiscoverActivity, "You clicked on item no. $position", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }

        })


        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)



    }


}