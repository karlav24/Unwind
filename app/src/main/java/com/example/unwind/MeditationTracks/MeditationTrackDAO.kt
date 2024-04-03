package com.example.unwind.MeditationTracks

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MeditationTrackDao {
    @Query("SELECT * FROM meditation_tracks")
    fun getAllTracks(): LiveData<List<MeditationTrack>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: MeditationTrack)

    @Query("DELETE FROM meditation_tracks")
    suspend fun deleteAllTracks()
}
