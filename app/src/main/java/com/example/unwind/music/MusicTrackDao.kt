package com.example.unwind.music

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MusicTrackDao {
    @Query("SELECT * FROM music_tracks")
    fun getAllMusicTracks(): List<MusicTrack>

    @Insert
    fun insertMusicTrack(musicTrack: MusicTrack)

    @Query("SELECT * FROM music_tracks WHERE genre = :genre")
    fun getAllMusicTracksByGenre(genre: String): List<MusicTrack>
}
