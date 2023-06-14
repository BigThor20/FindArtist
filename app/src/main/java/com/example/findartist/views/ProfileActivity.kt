package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findartist.R
import com.example.findartist.adapter.ImageAdapter
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.data.Datasource
import com.example.findartist.model.ArtistItemList
import com.example.findartist.model.ProfileViewModel
import com.example.findartist.model.RegisterViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity: AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: ProfileViewModel by viewModels()
    var userId = ""


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


        val profilePhotoImageView = findViewById<ImageView>(R.id.profile_photo)
        val profileNameTextView = findViewById<TextView>(R.id.profile_name)
        val profileJobTextView = findViewById<TextView>(R.id.profile_job)
        val profileRateTextView = findViewById<TextView>(R.id.profile_rate)
        val profilePhoneTextView = findViewById<TextView>(R.id.profile_phone)
        val profileMailTextView = findViewById<TextView>(R.id.profile_mail)
        val profileDescriptionTextView = findViewById<TextView>(R.id.profile_description)

        val artistId = intent.getStringExtra("id")
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
        profilePhoneTextView.text = artistItem?.phone.toString()
        profileMailTextView.text = artistItem?.email.toString()
        profileDescriptionTextView.text = artistItem?.description
        val photos = artistItem?.photos
        Log.i("photos", photos.toString())
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ImageAdapter(photos)

        // spinner
        val spinner =  findViewById<Spinner>(R.id.rateSpinner)
        val options = arrayOf("none","1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // GET CURRENT USER ID
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            userId = currentUser.uid
        } else {
            println("User not authenticated")
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedItem = options[position]

                // Perform action based on the selected item
                if (position == 0) {
                    viewModel.removeRating(userId, artistId)
                } else{
                    viewModel.addRating(userId, artistId, selectedItem.toInt())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case when no item is selected
            }
        }

        // send message
        var sendMessageButton = findViewById<Button>(R.id.goToChat)
        sendMessageButton.setOnClickListener{
            val intent = Intent(this@ProfileActivity, ChatActivity::class.java)
            // Puteți adăuga date suplimentare la intent, cum ar fi ID-ul sau alte informații despre chatItem
            intent.putExtra("userId", userId)
            intent.putExtra("artistId", artistId)
            startActivity(intent)
        }



    }

}