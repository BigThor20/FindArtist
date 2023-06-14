package com.example.findartist.views

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.findartist.R
import com.example.findartist.model.Artist
import com.example.findartist.model.RegisterViewModel
import com.example.findartist.model.User
import com.example.findartist.model.UserRole

class RegisterArtistActivity : AppCompatActivity() {
    private val CHANNEL_ID = "new_artist"
    private val notificationId = 101

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_artist_page)

        createNotificationChannel()
         var firstName = ""
         var job = ""
         var lastName = ""


        val registerButton = findViewById<Button>(R.id.registerArtist)
        registerButton.setOnClickListener {
            firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
            lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
            val mail = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.editTextRepeatPassword).text.toString()
            val city = findViewById<EditText>(R.id.editTextCity).text.toString()
            val industry = findViewById<Spinner>(R.id.industrySpinner).selectedItem.toString()
            job = findViewById<Spinner>(R.id.jobSpinner).selectedItem.toString()


            if (firstName.isNotEmpty() && lastName.isNotEmpty()
                && mail.isNotEmpty() && password.isNotEmpty()
                && city.isNotEmpty() && confirmPassword.isNotEmpty()) {
                val user = Artist(firstName, lastName, UserRole.ARTIST, mail, city, industry, job,
                    0f, "", "",ArrayList<String>(), ArrayList<Map<String, String>>());
                viewModel.register(user, password, confirmPassword)

            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registerStatus.observe(this, Observer { isRegistered ->
            if (isRegistered) {
                sendNotification(job, firstName, lastName )
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

    // functions for send notification
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Not title"
            val descriptionText = " helllo"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(job: String, firstName : String, lastName : String) {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New artist on the platform")
            .setContentTitle("There is a new $job on the platform. Check the app to see $firstName $lastName's profile.")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }


}
