package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findartist.R
import com.example.findartist.adapter.ImageAdapter
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.data.Datasource
import com.example.findartist.model.ArtistItemList
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity: AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        // add more images as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        //nav
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_discover -> {
                    val intent = Intent(this, DiscoverActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_chats -> {
                    val intent = Intent(this, ChatsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, MyProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        //end nav


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ImageAdapter(images)

        val profilePhotoImageView = findViewById<ImageView>(R.id.profile_photo)
        val profileNameTextView = findViewById<TextView>(R.id.profile_name)
        val profileJobTextView = findViewById<TextView>(R.id.profile_job)
        val profileRateTextView = findViewById<TextView>(R.id.profile_rate)
        val profilePhoneTextView = findViewById<TextView>(R.id.profile_phone)
        val profileMailTextView = findViewById<TextView>(R.id.profile_mail)
        val profileDescriptionTextView = findViewById<TextView>(R.id.profile_description)

        val id = intent.getStringExtra("id")
        val artistItem = intent.getSerializableExtra("artistItem") as? ArtistItemList

        Glide.with(this)
            .load(artistItem?.profilePhotoUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground) // Puneți o imagine placeholder
            .into(profilePhotoImageView)

        val artistName = artistItem?.firstName + " " + artistItem?.lastName
        profileNameTextView.text = artistName
        profileJobTextView.text = artistItem?.job
        profileRateTextView.text = artistItem?.rate.toString()
//        profilePhoneTextView.text = artistItem?.
        profileMailTextView.text = artistItem?.email.toString()
        profileDescriptionTextView.text = artistItem?.description

    }

}