package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.findartist.R
import com.example.findartist.model.RegisterViewModel

class RegisterUserActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_user_page)

        val registerButton = findViewById<Button>(R.id.registerUser)
        registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextName1).text.toString()
            val mail = findViewById<EditText>(R.id.editTextEmail1).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword1).text.toString()

            if (mail.isNotEmpty()) {
                viewModel.register(mail, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registerStatus.observe(this, Observer { isRegistered ->
            if (isRegistered) {
                openLoginActivity()
            } else {
                Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}