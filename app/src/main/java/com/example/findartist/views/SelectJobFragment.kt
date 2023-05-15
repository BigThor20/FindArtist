package com.example.findartist.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.findartist.R
import com.example.findartist.model.SelectJobViewModel

class SelectJobFragment : Fragment() {
    private val selectJobViewModel: SelectJobViewModel by viewModels()

    private lateinit var industrySpinner: Spinner
    private lateinit var jobSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_job, container, false)

        // Initialize the spinners
        industrySpinner = view.findViewById(R.id.industrySpinner)
        jobSpinner = view.findViewById(R.id.jobSpinner)

        populateIndustrySpinner()

        return view
    }

    private fun populateIndustrySpinner() {
        selectJobViewModel.fetchFirstSpinnerData().observe(viewLifecycleOwner) { firstSpinnerData ->
            val firstSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, firstSpinnerData)
            firstSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            industrySpinner.adapter = firstSpinnerAdapter

            // Set the onItemSelectedListener for the industry spinner
            industrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedOption = parent?.getItemAtPosition(position).toString()
                    populateJobSpinner(selectedOption)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No action needed
                }
            }
        }
    }

    private fun populateJobSpinner(selectedOption: String) {
        selectJobViewModel.fetchSecondSpinnerData(selectedOption).observe(viewLifecycleOwner) { secondSpinnerData ->
            val secondSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, secondSpinnerData)
            secondSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            jobSpinner.adapter = secondSpinnerAdapter
        }
    }
}
