package com.example.unwind.ui.listen

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.R
import com.example.unwind.music.DatabaseInitializer
import com.example.unwind.music.MusicTrack
import com.example.unwind.network.SavedTracksResponse
import com.example.unwind.network.SpotifyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AudioPlayerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playPauseButton: ImageView
    private lateinit var backButton: Button
    private lateinit var nextButton: ImageView
    private lateinit var previousButton: ImageView
    private var musicTracks: MutableList<MusicTrack> = mutableListOf()
    private var currentTrackIndex: Int = 0
    private lateinit var spotifyAccessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        initializeUIComponents()

        spotifyAccessToken = intent.getStringExtra("SPOTIFY_ACCESS_TOKEN") ?: ""
        val selectedGenre = intent.getStringExtra("SELECTED_GENRE")

        // Perform asynchronous operations to fetch music tracks
        GlobalScope.launch(Dispatchers.Main) {
            val localTracks = withContext(Dispatchers.IO) {
                DatabaseInitializer(this@AudioPlayerActivity).getAllTracksByGenre(selectedGenre ?: "")
            }
            musicTracks.addAll(localTracks)

            if (musicTracks.isNotEmpty()) {
                initializeMediaPlayer(0)
            } else if (spotifyAccessToken.isNotEmpty()) {
                fetchSpotifyTracks(spotifyAccessToken)
            } else {
                Log.e("AudioPlayerActivity", "No local songs found and no Spotify access token provided.")
                finish()
            }
        }
    }

    private fun initializeUIComponents() {
        playPauseButton = findViewById(R.id.play_pause_button)
        backButton = findViewById(R.id.button)
        nextButton = findViewById(R.id.next_track_button)
        previousButton = findViewById(R.id.prev_track_button)

        playPauseButton.setOnClickListener { onPlayPauseClick() }
        backButton.setOnClickListener { finish() }
        nextButton.setOnClickListener { playNextTrack() }
        previousButton.setOnClickListener { playPreviousTrack() }
    }

    private fun fetchSpotifyTracks(accessToken: String) {
        SpotifyApi.service.getUserSavedTracks("Bearer $accessToken").enqueue(object : Callback<SavedTracksResponse> {
            override fun onResponse(call: Call<SavedTracksResponse>, response: Response<SavedTracksResponse>) {
                if (response.isSuccessful) {
                    val tracks = response.body()?.items?.map { it.track } ?: listOf()
                    GlobalScope.launch(Dispatchers.Main) {
                        musicTracks.addAll(tracks.map { track ->
                            MusicTrack(title = track.name, artist = "Spotify Artist", genre = "Spotify", resourceId = null, previewUrl = track.preview_url)
                        })
                        if (musicTracks.isNotEmpty()) initializeMediaPlayer(0)
                    }
                } else {
                    Log.e("AudioPlayerActivity", "Error fetching Spotify tracks: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SavedTracksResponse>, t: Throwable) {
                Log.e("AudioPlayerActivity", "Failed to fetch Spotify tracks", t)
            }
        })
    }

    private fun initializeMediaPlayer(trackIndex: Int) {
        mediaPlayer?.release()  // Release any existing MediaPlayer instance
        val track = musicTracks[trackIndex]

        val mediaUri = if (track.previewUrl != null) Uri.parse(track.previewUrl) else Uri.parse("android.resource://${packageName}/${track.resourceId}")
        mediaPlayer = MediaPlayer.create(this, mediaUri)
        mediaPlayer?.apply {
            setOnCompletionListener {
                playNextTrack()
            }
            start()
        }
    }

    private fun onPlayPauseClick() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            } else {
                it.start()
            }
        }
    }

    private fun playNextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % musicTracks.size
        initializeMediaPlayer(currentTrackIndex)
    }

    private fun playPreviousTrack() {
        currentTrackIndex = if (currentTrackIndex > 0) currentTrackIndex - 1 else musicTracks.size - 1
        initializeMediaPlayer(currentTrackIndex)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }


}