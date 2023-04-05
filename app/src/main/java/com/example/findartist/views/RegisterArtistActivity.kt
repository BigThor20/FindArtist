package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.findartist.R

class RegisterArtistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_artist_page)

        val registerButton = findViewById<Button>(R.id.registerArtist)
        registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName).text.toString()



            if (name.isNotEmpty()) {
                // Registration logic here

                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("username", name)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}