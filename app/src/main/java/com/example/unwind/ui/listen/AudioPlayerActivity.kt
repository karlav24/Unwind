package com.example.unwind.ui.listen

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.R
import com.example.unwind.databinding.AudioPlayerBinding
import com.example.unwind.music.DatabaseInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playPauseButton: ImageView
    private lateinit var backButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        // Retrieve the selected genre from the intent extras
        val selectedGenre = intent.getStringExtra("SELECTED_GENRE")
        // Launch a coroutine to perform database operation asynchronously
        GlobalScope.launch(Dispatchers.Main) {
            // Use withContext to switch to the IO dispatcher for database operation
            val musicTracks = withContext(Dispatchers.IO) {
                DatabaseInitializer(this@AudioPlayerActivity).getAllTracksByGenre(selectedGenre ?: "")
            }

            if (musicTracks.isNotEmpty()) {
                // For simplicity, let's play the first song in the list
                val resourceId = musicTracks[0].resourceId
                mediaPlayer = MediaPlayer.create(this@AudioPlayerActivity, resourceId)
                mediaPlayer.setOnCompletionListener {
                    // Release MediaPlayer resources when playback completes
                    mediaPlayer.release()
                }
            } else {
                // If no songs found for the selected genre, finish the activity
                finish()
            }
        }

            playPauseButton = findViewById(R.id.play_pause_button)
            playPauseButton.setOnClickListener {
                onPlayPauseClick()
            }

            backButton = findViewById(R.id.button)
            backButton.setOnClickListener {
                finish()
            }
        }

        private fun onPlayPauseClick() {
            mediaPlayer.let {
                if (it.isPlaying) {
                    it.pause()
                    Log.d("AudioPlayerActivity", "Playback paused")
                } else {
                    it.start()
                    Log.d("AudioPlayerActivity", "Playback started")
                }
            }
        }


    override fun onDestroy() {
            super.onDestroy()
            // Release the MediaPlayer resources when the activity is destroyed
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }
        }
    }



