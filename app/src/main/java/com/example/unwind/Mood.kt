package com.example.unwind

import java.time.LocalDate

data class UserEntry(val date: LocalDate, val mood: Mood, var journalText: String? = null)
enum class Mood {
    ELATED, HAPPY, MEH, SAD, DEPRESSED, NOT_SET
}