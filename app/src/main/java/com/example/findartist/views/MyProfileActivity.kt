package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.findartist.R
import com.example.findartist.model.ArtistCardViewModel
import com.example.findartist.model.MyProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyProfileActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var myProfileViewModel: MyProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

        //nav
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.menu_profile
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
                    true
                }
                else -> false
            }
        }
        //get id
        val currentUser = FirebaseAuth.getInstance().currentUser
        var userId = ""

        // GET values from db

        if (currentUser != null) {
             userId = currentUser.uid
        } else {
            println("User not authenticated")
        }
        //end nav
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)

        val firstNameTextView = findViewById<TextView>(R.id.MyFirstName)
        val lastNameTextView = findViewById<TextView>(R.id.MyLastName)
        val phoneTextView = findViewById<TextView>(R.id.MyPhone)
        val descriptionTextView = findViewById<TextView>(R.id.MyDescription)

         myProfileViewModel.getFieldValueFromCollection("users", "firstName", userId) { fieldValue ->
            if (fieldValue != null) {
                firstNameTextView.text =fieldValue
            } else {
                Log.e("FetchDB", "firstName doesn't exist for this user")
            }
        }
        myProfileViewModel.getFieldValueFromCollection("users", "lastName", userId) { fieldValue ->
            if (fieldValue != null) {
                lastNameTextView.text =fieldValue
            } else {
                Log.e("FetchDB", "lastName doesn't exist for this user")
            }
        }
        myProfileViewModel.getFieldValueFromCollection("users", "phone", userId) { fieldValue ->
            if (fieldValue != null) {
                phoneTextView.text =fieldValue
            } else {
                Log.e("FetchDB", "phone doesn't exist for this user")
            }
        }

        myProfileViewModel.getFieldValueFromCollection("users", "description", userId) { fieldValue ->
            if (fieldValue != null) {
                descriptionTextView.text =fieldValue
            } else {
                Log.e("FetchDB", "description doesn't exist for this user")
            }
        }

        // UPDATE values from db

        val updateFirstNameButton = findViewById<Button>(R.id.updateFirstNameButton)
        updateFirstNameButton.setOnClickListener{
            val newFirstName = findViewById<EditText>(R.id.editProfileFirstName).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "firstName", userId, newFirstName.toString())
            }
        }

        val updateLastNameButton = findViewById<Button>(R.id.updateLastNameButton)
        updateLastNameButton.setOnClickListener{
            val newLastName = findViewById<EditText>(R.id.editProfileLastName).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "lastName", userId, newLastName.toString())
            }
        }

        val updatePhoneButton = findViewById<Button>(R.id.updatePhoneButton)
        updatePhoneButton.setOnClickListener{
            val newPhone = findViewById<EditText>(R.id.editProfilePhone).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "phone", userId, newPhone.toString())
            }
        }

        val updateDescriptionButton = findViewById<Button>(R.id.updateDescriptionButton)
        updateDescriptionButton.setOnClickListener{
            val newDescription = findViewById<EditText>(R.id.editProfileDescription).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "description", userId, newDescription.toString())
            }
        }



    }
}