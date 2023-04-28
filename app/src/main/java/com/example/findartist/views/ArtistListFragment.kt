package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.adapter.ItemAdapter
import com.example.findartist.model.ArtistCardViewModel

class ArtistListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_artist_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = ItemAdapter(requireContext(), emptyList()) // setează un adaptor gol

        viewModel = ViewModelProvider(this).get(ArtistCardViewModel::class.java)
        viewModel.fetchArtistsFromFirestore()

        viewModel.artistList.observe(viewLifecycleOwner, Observer { artistList ->
            val adapter = ItemAdapter(requireContext(), artistList)
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            recyclerView.adapter = adapter

            adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    startActivity(intent)
                }
            })
        })
    }
}
