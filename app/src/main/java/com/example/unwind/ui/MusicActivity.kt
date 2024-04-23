package com.example.unwind.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.MeditationTracks.MeditationTrack
import com.example.unwind.MeditationTracks.MeditationTrackAdapter
import com.example.unwind.MeditationTracks.MeditationViewModel
import com.example.unwind.R

class MusicActivity : AppCompatActivity() {

    private lateinit var viewModel: MeditationViewModel
    private lateinit var adapter: MeditationTrackAdapter
    private lateinit var recyclerViewTracks: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view_layout)

        recyclerViewTracks = findViewById(R.id.recyclerViewTracks)
        viewModel = ViewModelProvider(this).get(MeditationViewModel::class.java)
        adapter = MeditationTrackAdapter()

        recyclerViewTracks.adapter = adapter
        recyclerViewTracks.layoutManager = LinearLayoutManager(this)

        // Observe changes in tracks
        viewModel.allTracks.observe(this, { tracks ->
            adapter.setTracks(tracks)
        })

        // Insert sample tracks (if needed)
        insertSampleTracks()
    }

    private fun insertSampleTracks() {
        val sampleTracks = listOf(
            MeditationTrack(
                title = "Birds Singing",
                artist = "Nature Sounds",
                duration = 112, // duration in seconds.
                resourceId = R.raw.bird_singing
            ),
            MeditationTrack(
                title = "Ethereal",
                artist = "432 Hz Music",
                duration = 141, // duration in seconds.
                resourceId = R.raw.ethereal
            ),
            MeditationTrack(
                title = "Rain and Thunder",
                artist = "Nature Sounds",
                duration = 58, // duration in seconds.
                resourceId = R.raw.rain_thunder
            ),
            MeditationTrack(
                title = "Sleep",
                artist = "Sleeping Sounds",
                duration = 173, // duration in seconds.
                resourceId = R.raw.sleep
            )
        )

        for (track in sampleTracks) {
            viewModel.insert(track)
        }
    }
}
