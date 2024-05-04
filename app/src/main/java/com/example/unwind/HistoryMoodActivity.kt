package com.example.unwind

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.time.LocalDate

class HistoryMoodActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_mood)
        val journalEntryEditText = findViewById<EditText>(R.id.etJournalEntry)
        val addJournalButton = findViewById<Button>(R.id.btnAddJournal)
        val saveJournalButton = findViewById<Button>(R.id.btnSaveJournal)
        val cancelJournalButton = findViewById<Button>(R.id.btnCancelJournal)

        addJournalButton.setOnClickListener {
            // Make the EditText visible when Add button is clicked
            journalEntryEditText.visibility = View.VISIBLE
            cancelJournalButton.visibility = View.VISIBLE
            // Ppen the keyboard
            journalEntryEditText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(journalEntryEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        cancelJournalButton.setOnClickListener{
            journalEntryEditText.visibility = View.GONE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(journalEntryEditText.windowToken, 0)
            cancelJournalButton.visibility = View.GONE
        }

        saveJournalButton.setOnClickListener {
            val journalText = journalEntryEditText.text.toString()
            journalEntryEditText.text.clear()
            journalEntryEditText.visibility = View.GONE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(journalEntryEditText.windowToken, 0)
            cancelJournalButton.visibility = View.GONE
            // Assuming you have a method to determine the selected date
            val selectedDate = LocalDate.now() // This should be the date the user is adding a journal entry for
            addJournalEntry(selectedDate, journalText)
        }
        val entries = getAllEntries()
        var adapter = MoodEntryAdapter(entries)
        findViewById<RecyclerView>(R.id.rvMoodEntries).apply {
            layoutManager = LinearLayoutManager(this@HistoryMoodActivity)
            adapter = adapter
        }
    }

    private fun getAllEntries(): List<UserEntry> {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.all.mapNotNull { (_, value) ->
            Gson().fromJson(value as String, UserEntry::class.java)
        }
    }

    // Method to handle adding journal entries
    private fun addJournalEntry(date: LocalDate, text: String) {
        val entry = getEntryForDate(date) ?: UserEntry(date, Mood.NOT_SET, "")
        entry.journalText = text
        saveEntry(entry)
    }

    // Method to get an entry for a particular date
    private fun getEntryForDate(date: LocalDate): UserEntry? {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val entryJson = sharedPreferences.getString(date.toString(), null)
        return entryJson?.let { Gson().fromJson(it, UserEntry::class.java) }
    }
    private fun saveEntry(entry: UserEntry) {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(entry.date.toString(), Gson().toJson(entry))
            apply()
        }
    }
}

