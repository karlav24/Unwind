package com.example.unwind.user.journal

import UserEntryDatabase
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.R
import com.example.unwind.model.Mood
import com.example.unwind.model.UserEntry
import kotlinx.coroutines.launch
import java.time.LocalDate

class HistoryMoodActivity : AppCompatActivity() {
    private lateinit var adapter: MoodEntryAdapter
    private lateinit var database: UserEntryDatabase
    private lateinit var recyclerView: RecyclerView
    private var selectedMood: Mood? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_mood)
        Log.d("HistoryMoodActivity: OnCreate", "setting content view")
        database = UserEntryDatabase.getDatabase(applicationContext)
        Log.d("HistoryMoodActivity: OnCreate", "getting database")
        recyclerView = findViewById(R.id.rvMoodEntries)
        Log.d("HistoryMoodActivity: OnCreate", "getting rv")
        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.d("HistoryMoodActivity: OnCreate", "rv layout manager")
        loadJournalEntries()
        Log.d("HistoryMoodActivity: OnCreate", "loading journal entries")

        val journalEntryEditText: EditText = findViewById(R.id.etJournalEntry)
        val journalTitleEditText: EditText = findViewById(R.id.etJournalTitle)
        val addJournalButton: Button = findViewById(R.id.btnAddJournal)
        val saveJournalButton: Button = findViewById(R.id.btnSaveJournal)
        val cancelJournalButton: Button = findViewById(R.id.btnCancelJournal)

        intent.getStringExtra("selectedMood")?.let {
            selectedMood = Mood.valueOf(it)
        }
        Log.d("HistoryMoodActivity: OnCreate", "getting selected mood")

        setupButtonListeners(journalEntryEditText, journalTitleEditText, addJournalButton, saveJournalButton, cancelJournalButton)
        Log.d("HistoryMoodActivity: OnCreate", "set up button listeners")
        displayTodayMood()
        Log.d("HistoryMoodActivity: OnCreate", "displaying today mood")
    }

    private fun setupButtonListeners(journalEntryEditText: EditText, journalTitleEditText: EditText,
                                     addJournalButton: Button, saveJournalButton: Button, cancelJournalButton: Button) {
        addJournalButton.setOnClickListener {
            journalEntryEditText.visibility = View.VISIBLE
            cancelJournalButton.visibility = View.VISIBLE
            journalEntryEditText.requestFocus()
            showKeyboard(journalEntryEditText)
        }

        cancelJournalButton.setOnClickListener {
            journalEntryEditText.visibility = View.GONE
            journalTitleEditText.text.clear()
            journalEntryEditText.text.clear()
            cancelJournalButton.visibility = View.GONE
            hideKeyboard(journalEntryEditText)
        }

        saveJournalButton.setOnClickListener {
            val journalText = journalEntryEditText.text.toString()
            val journalTitle = journalTitleEditText.text.toString()
            if (selectedMood != null) {
                addJournalEntry(LocalDate.now(), selectedMood!!, journalTitle, journalText)
            }
            journalEntryEditText.visibility = View.GONE
            journalTitleEditText.text.clear()
            journalEntryEditText.text.clear()
            cancelJournalButton.visibility = View.GONE
            hideKeyboard(journalEntryEditText)
        }

        Log.d("HistoryMoodActivity: setupButtonListeners", "setting up buttons")
    }

    private fun loadJournalEntries() {
        lifecycleScope.launch {
            val entries = database.userEntryDao().getAllEntries()
            adapter = MoodEntryAdapter(entries) { entry -> /* Implement click behavior if necessary */ }
            recyclerView.adapter = adapter
        }
        Log.d("HistoryMoodActivity: loadJournalEntries", "load")
    }

    private fun displayTodayMood() {
        val currentDate = LocalDate.now().toString()
        lifecycleScope.launch {
            database.userEntryDao().findEntryByDate(currentDate)?.let {
                updateMoodDisplay(it.mood)
            }
        }
    }

    private fun updateMoodDisplay(mood: Mood) {
        val moodEmojiImageView: ImageView = findViewById(R.id.ivMoodEmoji)
        val moodDrawableRes = getMoodDrawable(mood)
        if (moodDrawableRes != 0) {
            moodEmojiImageView.setImageResource(moodDrawableRes)
            moodEmojiImageView.visibility = View.VISIBLE
        } else {
            moodEmojiImageView.visibility = View.GONE
        }
    }

    private fun getMoodDrawable(mood: Mood): Int = when (mood) {
        Mood.ELATED -> R.drawable.elated_emoji
        Mood.HAPPY -> R.drawable.happy_emoji
        Mood.MEH -> R.drawable.meh_emoji
        Mood.SAD -> R.drawable.sad_emoji
        Mood.DEPRESSED -> R.drawable.depressed_emoji
        Mood.NOT_SET -> 0
    }

    private fun addJournalEntry(date: LocalDate, mood: Mood, title: String, text: String) {
        lifecycleScope.launch {
            val entry = UserEntry(date = date, mood = mood, title = title, journalText = text)
            database.userEntryDao().insert(entry)
            loadJournalEntries()
        }
    }

    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


