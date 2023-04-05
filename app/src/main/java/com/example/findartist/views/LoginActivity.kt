package com.example.findartist.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.findartist.MainActivity
import com.example.findartist.R

class LoginActivity : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val registerButton = findViewById<Button>(R.id.buttonRegister)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterTypeActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.buttonLogin)
        loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.loginMailEditText).text.toString()
            val password = findViewById<EditText>(R.id.loginPasswordEditText).text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Authentication logic here

                val intent = Intent(this, DiscoverActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
