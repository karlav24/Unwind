package com.example.unwind.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyService {
    @GET("v1/me/playlists")
    fun getUserPlaylists(@Header("Authorization") authHeader: String): Call<PlaylistsResponse>

    // Define other endpoints might need
}

// A data class to match the JSON structure of responses from Spotify
data class PlaylistsResponse(val items: List<Playlist>)
data class Playlist(val id: String, val name: String, val tracks: Tracks)
data class Tracks(val total: Int)
