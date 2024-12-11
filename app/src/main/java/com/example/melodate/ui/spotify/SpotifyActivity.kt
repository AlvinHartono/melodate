package com.example.melodate.ui.spotify

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.melodate.R
import com.example.melodate.data.di.Injection
import com.example.melodate.data.preference.AuthTokenPreference
import com.example.melodate.data.preference.TopArtistsAdapter
import com.example.melodate.data.preference.TopTracksAdapter
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import kotlinx.coroutines.launch

class SpotifyActivity : AppCompatActivity() {

    private lateinit var authTokenPreference: AuthTokenPreference
    private val spotifyViewModel: SpotifyViewModel by viewModels {
        SpotifyViewModelFactory(
            Injection.provideSpotifyRepository(),
            Injection.provideSpotifyPreference(this)
        )
    }

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    private lateinit var topArtistsAdapter: TopArtistsAdapter
    private lateinit var topTracksAdapter: TopTracksAdapter
//    private var isDataUpdated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authTokenPreference = Injection.provideAuthTokenPreference(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spotify)

        setupRecyclerViews()

        spotifyViewModel.topArtists.observe(this) { artists ->
            topArtistsAdapter.submitList(artists)
//            checkAndUpdateSpotifyData()
        }

        spotifyViewModel.topTracks.observe(this) { tracks ->
            topTracksAdapter.submitList(tracks)
//            checkAndUpdateSpotifyData()
        }

        spotifyViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//            isDataUpdated = false
        }

        authViewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//                isDataUpdated = false
            }
        }


        lifecycleScope.launch {
            spotifyViewModel.getSpotifyTokenData().collect { tokenData ->
                if (tokenData != null) {
                    fetchSpotifyData(tokenData.accessToken)
                } else {
                    Log.d("SpotifyActivity", "No token found in DataStore")
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        val artistsRecyclerView = findViewById<RecyclerView>(R.id.rv_top_artists)
        topArtistsAdapter = TopArtistsAdapter()
        artistsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        artistsRecyclerView.adapter = topArtistsAdapter

        val tracksRecyclerView = findViewById<RecyclerView>(R.id.rv_top_tracks)
        topTracksAdapter = TopTracksAdapter()
        tracksRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        tracksRecyclerView.adapter = topTracksAdapter
    }

    private fun fetchSpotifyData(token: String) {
        spotifyViewModel.fetchTopArtists(token)
        spotifyViewModel.fetchTopTracks(token)
    }

//    private fun checkAndUpdateSpotifyData() {
//        lifecycleScope.launch {
//            authTokenPreference.getUserId().collect { userId ->
//                if (userId.isNullOrBlank()) {
//                    Log.e("SpotifyActivity", "User ID is null or empty, skipping update.")
//                    return@collect
//                }
//
//                val topArtists = spotifyViewModel.topArtists.value
//                val topTracks = spotifyViewModel.topTracks.value
//
//                if (!topArtists.isNullOrEmpty() && !topTracks.isNullOrEmpty() && !isDataUpdated) {
//                    authViewModel.updateSpotifyData(userId, topArtists, topTracks)
//                    isDataUpdated = true
//                } else {
//                    Log.d("SpotifyActivity", "Top artists or tracks are missing, skipping update.")
//                }
//            }
//        }
//    }
}


