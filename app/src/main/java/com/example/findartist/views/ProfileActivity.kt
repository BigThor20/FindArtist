package com.example.findartist.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ImageAdapter
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.data.Datasource

class ProfileActivity: AppCompatActivity() {
    private val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        // add more images as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ImageAdapter(images)
    }

}