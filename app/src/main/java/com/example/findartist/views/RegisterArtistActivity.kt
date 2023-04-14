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
import com.example.findartist.model.User
import com.example.findartist.model.UserRole

class RegisterArtistActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_artist_page)

        val registerButton = findViewById<Button>(R.id.registerArtist)
        registerButton.setOnClickListener {
            val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
            val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
            val mail = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.editTextRepeatPassword).text.toString()
            val city = findViewById<EditText>(R.id.editTextCity).text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty()
                && mail.isNotEmpty() && password.isNotEmpty()
                && city.isNotEmpty() && confirmPassword.isNotEmpty()) {
                val user = User(firstName, lastName, UserRole.ARTIST, mail, city);
                viewModel.register(user, password, confirmPassword)
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

        viewModel.validationError.observe(this, Observer { validationError ->
            if (validationError.isNotEmpty()) {
                Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
