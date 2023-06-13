package com.example.findartist.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.findartist.R

class MyPortfolioFragment : Fragment() {
    private lateinit var uploadPortfolioImage1Button: Button
    private lateinit var deletePortfolioImage1Button: Button
    private lateinit var uploadPortfolioImage2Button: Button
    private lateinit var deletePortfolioImage2Button: Button

    // ... Define other buttons and views
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_portfolio, container, false)
    }

}