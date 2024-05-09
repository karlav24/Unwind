package com.example.unwind.utils

import androidx.room.TypeConverter
import com.example.unwind.model.Mood

class MoodConverter {
    @TypeConverter
    fun fromMood(mood: Mood): String {
        return mood.name
    }

    @TypeConverter
    fun toMood(mood: String): Mood {
        return Mood.valueOf(mood)
    }
}
