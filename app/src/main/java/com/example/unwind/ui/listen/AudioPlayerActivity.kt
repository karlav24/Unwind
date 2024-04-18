package com.example.unwind.ui.listen

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.R
import com.example.unwind.databinding.AudioPlayerBinding

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: AudioPlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the resource ID from the intent extras
        val resourceId = intent.getIntExtra("TRACK_RESOURCE_ID", -1)

        if (resourceId != -1) {
            // Initialize MediaPlayer with the selected track
            mediaPlayer = MediaPlayer.create(this, resourceId)
            mediaPlayer?.setOnCompletionListener {
                // Release MediaPlayer resources when playback completes
                mediaPlayer?.release()
            }
        } else {
            // If resource ID is invalid, finish the activity
            finish()
        }
        playButton = findViewById(R.id.play_pause_button)
        playButton.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause() // Pause playback if currently playing
                } else {
                    it.start() // Resume playback if paused
                }
            }


        }

        fun onDestroy() {
            super.onDestroy()
            // Release the MediaPlayer resources when the activity is destroyed
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}
