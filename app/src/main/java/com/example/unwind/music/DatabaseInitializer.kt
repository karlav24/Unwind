package com.example.unwind.music

import android.content.Context
import com.example.unwind.R

class DatabaseInitializer(private val context: Context) {
    private val musicTrackDao = MusicDatabase.getDatabase(context).musicTrackDao()

    fun initializeDatabase() {
        if (musicTrackDao.getAllMusicTracks().isEmpty()) {
            val musicTracks = loadMusicTracksFromRaw(context)
            for (musicTrack in musicTracks) {
                musicTrackDao.insertMusicTrack(musicTrack)
            }
        }
    }

    private fun loadMusicTracksFromRaw(context: Context): List<MusicTrack> {
        val musicTracks = mutableListOf<MusicTrack>()
        val resources = context.resources
        val rawResources = resources.obtainTypedArray(R.array.raw_music_tracks)

        for (i in 0 until rawResources.length()) {
            val resourceId = rawResources.getResourceId(i, 0)
            val resourceName = resources.getResourceEntryName(resourceId)
            val genre = extractGenreFromFileName(resourceName)
            val musicTrack = MusicTrack(title = resourceName, artist = "Unknown", genre = genre)
            musicTracks.add(musicTrack)
        }

        rawResources.recycle()
        return musicTracks
    }

    private fun extractGenreFromFileName(fileName: String): String {
        // Extract the genre from the filename
        // Assuming the genre is the first word before an underscore
        return fileName.substringBefore("_")
    }

}
