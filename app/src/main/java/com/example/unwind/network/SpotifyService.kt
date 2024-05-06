package com.example.unwind.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyService {
    // Fetch the user's playlists
    @GET("v1/me/playlists")
    fun getUserPlaylists(@Header("Authorization") authHeader: String): Call<PlaylistsResponse>

    // Fetch the user's saved tracks
    @GET("v1/me/tracks")
    fun getUserSavedTracks(@Header("Authorization") authHeader: String): Call<SavedTracksResponse>
}

// Data class to match the JSON structure of playlists response from Spotify
data class PlaylistsResponse(
    val items: List<Playlist>
)

data class Playlist(
    val id: String,
    val name: String,
    val tracks: Tracks
)

data class Tracks(
    val total: Int
)

// Data class to match the JSON structure of saved tracks response from Spotify
data class SavedTracksResponse(
    val items: List<SavedTrackItem>
)

data class SavedTrackItem(
    val track: SpotifyTrack
)

data class SpotifyTrack(
    val id: String,
    val name: String,
    val preview_url: String  // The URL of the preview clip for the track (if available)
)
