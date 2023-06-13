package com.example.findartist.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.findartist.R
import com.example.findartist.model.MyProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyPortfolioFragment : Fragment() {
    private lateinit var myProfileViewModel: MyProfileViewModel
    private val pickImage = 100
    private var imageUri: Uri? = null
    var userId = ""
    private lateinit var imageView: ImageView
    private var position: Int = 0

    // ... Define other buttons and views
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_portfolio, container, false)

        // GET CURRENT USER ID
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            userId = currentUser.uid
        } else {
            println("User not authenticated")
        }
        myProfileViewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)


        // find buttons and images
        val uploadPortfolioImage1Button = view.findViewById<Button>(R.id.uploadPortfolioImage1)
        val deletePortfolioImage1Button = view.findViewById<Button>(R.id.deletePortfolioImage1)
        val imagePortfolio1 = view.findViewById<ImageView>(R.id.myPortfolioImage1)
        val uploadPortfolioImage2Button = view.findViewById<Button>(R.id.uploadPortfolioImage2)
        val deletePortfolioImage2Button = view.findViewById<Button>(R.id.deletePortfolioImage2)
        val imagePortfolio2 = view.findViewById<ImageView>(R.id.myPortfolioImage2)
        val uploadPortfolioImage3Button = view.findViewById<Button>(R.id.uploadPortfolioImage3)
        val deletePortfolioImage3Button = view.findViewById<Button>(R.id.deletePortfolioImage3)
        val imagePortfolio3 = view.findViewById<ImageView>(R.id.myPortfolioImage3)
        val uploadPortfolioImage4Button = view.findViewById<Button>(R.id.uploadPortfolioImage4)
        val deletePortfolioImage4Button = view.findViewById<Button>(R.id.deletePortfolioImage4)
        val imagePortfolio4 = view.findViewById<ImageView>(R.id.myPortfolioImage4)
        val uploadPortfolioImage5Button = view.findViewById<Button>(R.id.uploadPortfolioImage5)
        val deletePortfolioImage5Button = view.findViewById<Button>(R.id.deletePortfolioImage5)
        val imagePortfolio5 = view.findViewById<ImageView>(R.id.myPortfolioImage5)

        // get values for image views
        getValuesFromDb(imagePortfolio1, imagePortfolio2, imagePortfolio3, imagePortfolio4, imagePortfolio5)

        // listeners for update and delete buttons
        //image1
        uploadPortfolioImage1Button.setOnClickListener{
            imageView = imagePortfolio1
            position = 0;
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        deletePortfolioImage1Button.setOnClickListener{
            imagePortfolio1.setImageResource(R.drawable.upload)
            position = 0;
            GlobalScope.launch {
                myProfileViewModel.deleteElementFromAnArrayFromCollection(
                    "users", "photos", userId, position)
            }
        }
        //image2
        uploadPortfolioImage2Button.setOnClickListener{
            imageView = imagePortfolio2
            position = 1;
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        deletePortfolioImage2Button.setOnClickListener{
            imagePortfolio2.setImageResource(R.drawable.upload)
            position = 1
            GlobalScope.launch {
                myProfileViewModel.deleteElementFromAnArrayFromCollection(
                    "users", "photos", userId, position)
            }
        }
        //image3
        uploadPortfolioImage3Button.setOnClickListener{
            imageView = imagePortfolio3
            position = 2
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        deletePortfolioImage3Button.setOnClickListener{
            imagePortfolio3.setImageResource(R.drawable.upload)
            position = 2;
            GlobalScope.launch {
                myProfileViewModel.deleteElementFromAnArrayFromCollection(
                    "users", "photos", userId, position)
            }
        }
        //image4
        uploadPortfolioImage4Button.setOnClickListener{
            imageView = imagePortfolio4
            position = 3
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        deletePortfolioImage4Button.setOnClickListener{
            imagePortfolio4.setImageResource(R.drawable.upload)
            position = 3
            GlobalScope.launch {
                myProfileViewModel.deleteElementFromAnArrayFromCollection(
                    "users", "photos", userId, position)
            }
        }
        //image1
        uploadPortfolioImage5Button.setOnClickListener{
            imageView = imagePortfolio5
            position = 4
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        deletePortfolioImage5Button.setOnClickListener{
            imagePortfolio5.setImageResource(R.drawable.upload)
            position = 4
            GlobalScope.launch {
                myProfileViewModel.deleteElementFromAnArrayFromCollection(
                    "users", "photos", userId, position)
            }
        }

        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)

            // Upload the image to Firebase Storage
            imageUri?.let { uri ->
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("portfolio/${userId}/${System.currentTimeMillis()}.jpg")

                val uploadTask = imageRef.putFile(uri)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        // Use the image URL as needed (e.g., store it in Firestore)
                        Log.i("IMAAAAAAGEEEE", imageUrl)
                        GlobalScope.launch {
                            myProfileViewModel.addElementToAnArrayFromCollection(
                                "users", "photos", userId, imageUrl, position)
                        }
                    }
                }.addOnFailureListener { exception ->
                    // Error uploading image
                    // Handle the error as needed
                }
            }
        }
    }
    fun getValuesFromDb(
        imagePortfolio1: ImageView,
        imagePortfolio2: ImageView,
        imagePortfolio3: ImageView,
        imagePortfolio4: ImageView,
        imagePortfolio5: ImageView,
    ) {
        loadImageViewFromDb(imagePortfolio1, 0)
        loadImageViewFromDb(imagePortfolio2, 1)
        loadImageViewFromDb(imagePortfolio3, 2)
        loadImageViewFromDb(imagePortfolio4, 3)
        loadImageViewFromDb(imagePortfolio5, 4)
    }

    private fun loadImageViewFromDb(imageView: ImageView, position: Int) {
        myProfileViewModel.getArrayElementFromCollection(
            "users",
            "photos",
            userId,
            position
        ) { fieldValue ->
            if (fieldValue != null) {
                Glide.with(this)
                    .load(fieldValue)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground) // Puneți o imagine placeholder
                    .into(imageView)
            } else {
                Log.i("FetchDB", "Photo URL at position $position doesn't exist for this user")
            }
        }
    }


}