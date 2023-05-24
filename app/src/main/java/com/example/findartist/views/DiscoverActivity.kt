package com.example.findartist.views

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.findartist.R
import com.example.findartist.location.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class DiscoverActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_page)

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
