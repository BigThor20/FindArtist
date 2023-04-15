package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.findartist.R
import com.example.findartist.model.Artist
import com.example.findartist.model.RegisterViewModel
import com.example.findartist.model.User
import com.example.findartist.model.UserRole

class RegisterArtistActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_artist_page)

        val firstSpinner: Spinner = findViewById(R.id.industrySpinner)
        val secondSpinner: Spinner = findViewById(R.id.jobSpinner)

        // Variables to store selected values
        var industry = ""
        var job = ""

        // Obțineți datele de la ViewModel pentru primul spinner
        viewModel.fetchFirstSpinnerData().observe(this, Observer { firstSpinnerData ->
            val firstSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, firstSpinnerData)
            firstSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            firstSpinner.adapter = firstSpinnerAdapter
        })

        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = firstSpinner.selectedItem.toString()

                // Store the selected value for industry
                industry = selectedOption

                // Obțineți datele de la ViewModel pentru al doilea spinner
                viewModel.fetchSecondSpinnerData(selectedOption).observe(this@RegisterArtistActivity, Observer { secondSpinnerData ->
                    val secondSpinnerAdapter = ArrayAdapter(this@RegisterArtistActivity, android.R.layout.simple_spinner_item, secondSpinnerData)
                    secondSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    secondSpinner.adapter = secondSpinnerAdapter
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nimic de făcut
            }
        }

        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                job = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
                val user = Artist(firstName, lastName, UserRole.ARTIST, mail, city, industry, job);
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
