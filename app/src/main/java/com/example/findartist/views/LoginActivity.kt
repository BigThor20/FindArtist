package com.example.findartist.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.findartist.R
import com.example.findartist.model.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

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
            val email = findViewById<EditText>(R.id.loginMailEditText).text.toString()
            val password = findViewById<EditText>(R.id.loginPasswordEditText).text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginStatus.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                openMainActivity()
            } else {
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openMainActivity() {
        val intent = Intent(this, DiscoverActivity::class.java)
        startActivity(intent)
        finish()
    }

}
