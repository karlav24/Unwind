package com.example.unwind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.unwind.MeditationTracks.MeditationTrack
import com.example.unwind.MeditationTracks.MeditationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MeditationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MeditationViewModel::class.java)

        // Observe changes in tracks
        /*viewModel.allTracks.observe(this, { tracks ->
            // Update UI with the list of tracks
        })*/

        var newTrack = MeditationTrack(
            title = "Birds Singing",
            artist = "Nature Sounds",
            duration = 112, // duration in seconds.
            resourceId = R.raw.bird_singing
        )
        viewModel.insert(newTrack)
        newTrack = MeditationTrack(
            title = "Ethereal",
            artist = "432 Hz Music",
            duration = 141, // duration in seconds.
            resourceId = R.raw.ethereal
        )
        viewModel.insert(newTrack)
        newTrack = MeditationTrack(
            title = "Rain and Thunder",
            artist = "Nature Sounds",
            duration = 58, // duration in seconds.
            resourceId = R.raw.rain_thunder
        )
        viewModel.insert(newTrack)
        newTrack = MeditationTrack(
            title = "Sleep",
            artist = "Sleeping Sounds",
            duration = 173, // duration in seconds.
            resourceId = R.raw.rain_thunder
        )
        viewModel.insert(newTrack)
    }
}
