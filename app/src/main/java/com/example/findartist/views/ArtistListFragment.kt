package com.example.findartist.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    companion object {
        fun newInstance(industry: String, job : String, location : String, name : String)
        : ArtistListFragment {
            val fragment = ArtistListFragment()
            val args = Bundle()
            args.putString("industry", industry)
            args.putString("job", job)
            args.putString("location", location)
            args.putString("name", name)
            fragment.arguments = args
            return fragment
        }
    }

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

        // incarca lista cu artisti
        viewModel = ViewModelProvider(this).get(ArtistCardViewModel::class.java)
        // get parameters for filter artist list
        val industry = arguments?.getString("industry")
        val job = arguments?.getString("job")
        val location = arguments?.getString("location")
        val name = arguments?.getString("name")
        Log.i("PARAMETERS", "PArametrii dati: $industry $job $location $name")

        viewModel.fetchArtistsFromFirestore(industry, job, location, name)

        viewModel.artistList.observe(viewLifecycleOwner, Observer { artistList ->
            val adapter = ItemAdapter(requireContext(), artistList)

            adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, id: String) {
                    val intent = Intent(requireActivity(), ProfileActivity::class.java)
                    val artistItem = artistList[position]
                    intent.putExtra("id", id)
                    intent.putExtra("artistItem", artistItem)
                    startActivity(intent)
                }
            })

            recyclerView.adapter = adapter
        })

    }

}
