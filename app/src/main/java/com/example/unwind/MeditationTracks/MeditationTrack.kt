package com.example.unwind.MeditationTracks
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meditation_tracks")
data class MeditationTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val artist: String,
    val duration: Long,
    val resourceId: Int
)