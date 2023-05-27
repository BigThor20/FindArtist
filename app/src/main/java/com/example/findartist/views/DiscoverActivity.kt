package com.example.findartist.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.findartist.R
import com.example.findartist.location.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView

class DiscoverActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_page)

        //nav
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_discover -> {
                    true
                }
                R.id.menu_chats -> {
                    val intent = Intent(this, ChatsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, MyProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        //end nav

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ArtistListFragment())
                .commit()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val getLocationButton = this.findViewById<Button>(R.id.getLocationButton)
        val cityInput = this.findViewById<EditText>(R.id.location_filter)
        getLocationButton.setOnClickListener {
            requestLocation(cityInput)
        }

        val searchButton = this.findViewById<Button>(R.id.searchArtist)
        searchButton.setOnClickListener{
            Log.i("FILTRARE", "Se cauta artisti")
            val industry = findViewById<Spinner>(R.id.industrySpinner).selectedItem.toString()
            val job = findViewById<Spinner>(R.id.jobSpinner).selectedItem.toString()
            val location = findViewById<EditText>(R.id.location_filter).text.toString()
            val name = findViewById<EditText>(R.id.name_filter).text.toString()
            reloadArtistListFragment(industry, job, location, name)

        }
    }

    private fun reloadArtistListFragment(industry : String, job : String, location : String, name : String) {
        val fragment = ArtistListFragment.newInstance(industry, job, location, name)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun requestLocation(cityInput : EditText) {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        // Handle the location here
                        // For example, you can get the latitude and longitude
                        val cityName = LocationUtils(applicationContext).getCityName(location)
                        cityInput.setText(cityName)
                        Log.i("Location", "Locatia ta curenta este: $cityName")
                    }
                }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}
