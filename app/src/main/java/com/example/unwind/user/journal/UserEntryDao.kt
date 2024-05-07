package com.example.unwind.user.journal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.unwind.model.UserEntry

@Dao
interface UserEntryDao {
    @Insert
    suspend fun insert(entry: UserEntry)

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    suspend fun getAllEntries(): List<UserEntry>

    @Query("SELECT * FROM journal_entries WHERE date = :date LIMIT 1")
    suspend fun findEntryByDate(date: String): UserEntry?
}
