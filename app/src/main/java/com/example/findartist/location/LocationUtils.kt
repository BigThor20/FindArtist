package com.example.findartist.location
import android.content.Context
import android.location.Geocoder
import android.location.Location
import java.io.IOException
import java.util.*

class LocationUtils(private val context: Context) {

    fun getCityName(location: Location): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var cityName = ""

        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                cityName = address.locality ?: ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return cityName
    }
}
