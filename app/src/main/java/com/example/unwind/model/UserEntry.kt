package com.example.unwind.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "journal_entries")
data class UserEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: LocalDate,
    val mood: Mood,
    var title: String? = null,
    var journalText: String? = null
)

enum class Mood {
    ELATED, HAPPY, MEH, SAD, DEPRESSED, NOT_SET
}
