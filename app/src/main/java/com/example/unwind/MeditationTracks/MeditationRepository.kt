package com.example.unwind.MeditationTracks

import androidx.lifecycle.LiveData

class MeditationRepository(private val dao: MeditationTrackDao) {

    val allTracks: LiveData<List<MeditationTrack>> = dao.getAllTracks()

    suspend fun insert(track: MeditationTrack) {
        dao.insertTrack(track)
    }

    suspend fun deleteAll() {
        dao.deleteAllTracks()
    }
}
