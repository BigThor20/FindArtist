package com.example.findartist.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.findartist.R

class DiscoverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.discover_page)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ArtistListFragment())
                .commit()
        }
    }
}
