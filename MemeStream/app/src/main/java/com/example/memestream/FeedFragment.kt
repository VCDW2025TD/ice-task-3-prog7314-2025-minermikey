package com.example.memestream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FeedFragment : Fragment() {

    private val feedViewModel: FeedViewModel by viewModels()
    private lateinit var adapter: MemeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvMemes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MemeAdapter()
        recyclerView.adapter = adapter

        // Observe the trending memes
        feedViewModel.trendingMemes.observe(viewLifecycleOwner) { response ->
            adapter.setMemes(response.data)
        }

        // Observe errors
        feedViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        // Fetch memes
        feedViewModel.fetchTrendingMemes(25)

        return view
    }
}
