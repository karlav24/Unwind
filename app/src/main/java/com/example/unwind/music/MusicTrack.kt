package com.example.unwind.music

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_tracks")
data class MusicTrack(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val artist: String,
    val genre: String,
    val resourceId: Int?,  // Make this nullable
    val previewUrl: String? = null
)
