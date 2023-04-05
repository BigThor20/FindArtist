package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.findartist.R

class RegisterTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_type_page)

        val artistRegisterButton = findViewById<Button>(R.id.buttonArtistType)
        artistRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterArtistActivity::class.java)
            startActivity(intent)
        }

        val userRegisterButton = findViewById<Button>(R.id.buttonUserType)
        userRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }
}
