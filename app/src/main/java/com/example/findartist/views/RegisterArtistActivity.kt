package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.findartist.FirestoreHelper
import com.example.findartist.R
import com.google.firebase.auth.FirebaseAuth

class RegisterArtistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_artist_page)

        val registerButton = findViewById<Button>(R.id.registerArtist)
        val auth = FirebaseAuth.getInstance()
        registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName).text.toString()
            val mail = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()


            if (mail.isNotEmpty() ) {
                // Registration logic here
                auth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // User was registered successfully.
                            val user = auth.currentUser
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("username", user)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show()
                        }
                    }

//                val db = FirestoreHelper().db
//                db.collection("users").document("john_doe").set(user)


            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}