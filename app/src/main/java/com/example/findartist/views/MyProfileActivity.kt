package com.example.findartist.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.findartist.R
import com.example.findartist.model.MyProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyProfileActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var myProfileViewModel: MyProfileViewModel
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    var userId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_profile)

        // NAVBAR
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
        // GET CURRENT USER ID
        val currentUser = FirebaseAuth.getInstance().currentUser


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
         imageView = findViewById<ImageView>(R.id.actual_profile_photo)

        myProfileViewModel.getFieldValueFromCollection("users", "firstName", userId) { fieldValue ->
            if (fieldValue != null) {
                firstNameTextView.text = fieldValue
            } else {
                Log.e("FetchDB", "firstName doesn't exist for this user")
            }
        }
        myProfileViewModel.getFieldValueFromCollection("users", "lastName", userId) { fieldValue ->
            if (fieldValue != null) {
                lastNameTextView.text = fieldValue
            } else {
                Log.e("FetchDB", "lastName doesn't exist for this user")
            }
        }
        myProfileViewModel.getFieldValueFromCollection("users", "phone", userId) { fieldValue ->
            if (fieldValue != null) {
                phoneTextView.text = fieldValue
            } else {
                Log.e("FetchDB", "phone doesn't exist for this user")
            }
        }

        myProfileViewModel.getFieldValueFromCollection(
            "users",
            "description",
            userId
        ) { fieldValue ->
            if (fieldValue != null) {
                descriptionTextView.text = fieldValue
            } else {
                Log.e("FetchDB", "description doesn't exist for this user")
            }
        }

        myProfileViewModel.getFieldValueFromCollection(
            "users",
            "profilePhotoUrl",
            userId
        ) { fieldValue ->
            if (fieldValue != null) {
                Glide.with(this)
                    .load(fieldValue)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground) // Puneți o imagine placeholder
                    .into(imageView)
            } else {
                Log.e("FetchDB", "profile photo URL doesn't exist for this user")
            }
        }

        // UPDATE values from db

        val updateFirstNameButton = findViewById<Button>(R.id.updateFirstNameButton)
        updateFirstNameButton.setOnClickListener {
            val newFirstName = findViewById<EditText>(R.id.editProfileFirstName).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "firstName", userId, newFirstName.toString()
                )
            }
        }

        val updateLastNameButton = findViewById<Button>(R.id.updateLastNameButton)
        updateLastNameButton.setOnClickListener {
            val newLastName = findViewById<EditText>(R.id.editProfileLastName).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "lastName", userId, newLastName.toString()
                )
            }
        }

        val updatePhoneButton = findViewById<Button>(R.id.updatePhoneButton)
        updatePhoneButton.setOnClickListener {
            val newPhone = findViewById<EditText>(R.id.editProfilePhone).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "phone", userId, newPhone.toString()
                )
            }
        }

        val updateDescriptionButton = findViewById<Button>(R.id.updateDescriptionButton)
        updateDescriptionButton.setOnClickListener {
            val newDescription = findViewById<EditText>(R.id.editProfileDescription).text
            GlobalScope.launch {
                myProfileViewModel.updateFieldValueFromCollection(
                    "users", "description", userId, newDescription.toString()
                )
            }
        }

        FirebaseStorage.getInstance()
        // UPLOAD PHOTO
        button = findViewById(R.id.buttonLoadPicture)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)

            // Upload the image to Firebase Storage
            imageUri?.let { uri ->
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("profile_photos/${System.currentTimeMillis()}.jpg")

                val uploadTask = imageRef.putFile(uri)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        // Use the image URL as needed (e.g., store it in Firestore)
                        Log.i("IMAAAAAAGEEEE", imageUrl)
                        GlobalScope.launch {
                            myProfileViewModel.updateFieldValueFromCollection(
                                "users", "profilePhotoUrl", userId, imageUrl)
                        }
                    }
                }.addOnFailureListener { exception ->
                    // Error uploading image
                    // Handle the error as needed
                }
            }
        }
    }


}
