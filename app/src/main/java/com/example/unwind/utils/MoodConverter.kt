package com.example.unwind.utils

import androidx.room.TypeConverter
import com.example.unwind.model.Mood

class MoodConverter {
    @TypeConverter
    fun fromMood(mood: Mood?): String? {
        return mood?.name
    }

    @TypeConverter
    fun toMood(moodName: String?): Mood? {
        return moodName?.let { Mood.valueOf(it) }
    }
}