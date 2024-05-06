package com.example.unwind.music

import android.content.Context
import android.util.Log
import com.example.unwind.R
import com.example.unwind.user.User
import com.example.unwind.user.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializer(private val context: Context) {
    private val musicTrackDao = MusicDatabase.getDatabase(context).musicTrackDao()
    private val userDao = UserDatabase.getDatabase(context).userDao()
    suspend fun initializeDatabase() {
        initializeMusicDatabase()
        initializeUserDatabase()
    }
    suspend fun initializeMusicDatabase() {
        withContext(Dispatchers.IO) {
            if (musicTrackDao.getAllMusicTracks().isEmpty()) {
                val musicTracks = loadMusicTracksFromRaw(context)
                for (musicTrack in musicTracks) {
                    musicTrackDao.insertMusicTrack(musicTrack)
                }
            }
        }
    }
    suspend fun initializeUserDatabase() {
        withContext(Dispatchers.IO) {
            val userDao = UserDatabase.getDatabase(context).userDao()

            // Check if the user table is empty
            if (userDao.getAllUsers().isEmpty()) {
                // Perform database operations within this coroutine scope
                val adminUser = User(name = "Admin", email = "admin@example.com", password = "admin123")
                userDao.insert(adminUser)
            }
        }
    }
    suspend fun getAllTracksByGenre(genre: String): List<MusicTrack> {
        val lowercaseGenre = genre.lowercase()
        return withContext(Dispatchers.IO) {
            val musicTracks = musicTrackDao.getAllMusicTracksByGenre(lowercaseGenre)
            Log.d("DatabaseInitializer", "Retrieved ${musicTracks.size} tracks for genre: $lowercaseGenre")
            musicTracks
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

            // Create a MusicTrack object with the resource ID
            val musicTrack = MusicTrack(
                title = resourceName,
                artist = "Unknown",
                genre = genre,
                resourceId = resourceId // Pass the obtained resource ID
            )
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
