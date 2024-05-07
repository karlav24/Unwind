package com.example.unwind.user.journal

import UserEntryDatabase
import android.content.Context
import android.os.Bundle
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

        database = UserEntryDatabase.getDatabase(this)
        recyclerView = findViewById(R.id.rvMoodEntries)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadJournalEntries()

        val journalEntryEditText: EditText = findViewById(R.id.etJournalEntry)
        val journalTitleEditText: EditText = findViewById(R.id.etJournalTitle)
        val addJournalButton: Button = findViewById(R.id.btnAddJournal)
        val saveJournalButton: Button = findViewById(R.id.btnSaveJournal)
        val cancelJournalButton: Button = findViewById(R.id.btnCancelJournal)

        intent.getStringExtra("selectedMood")?.let {
            selectedMood = Mood.valueOf(it)
        }

        setupButtonListeners(journalEntryEditText, journalTitleEditText, addJournalButton, saveJournalButton, cancelJournalButton)

        displayTodayMood()
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
    }

    private fun loadJournalEntries() {
        lifecycleScope.launch {
            val entries = database.userEntryDao().getAllEntries()
            adapter = MoodEntryAdapter(entries) { entry -> /* Implement click behavior if necessary */ }
            recyclerView.adapter = adapter
        }
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
//package com.example.unwind.user.journal
//
//import UserEntryDatabase
//import android.content.Context
//import android.os.Bundle
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.unwind.R
//import com.example.unwind.model.Mood
//import com.example.unwind.model.UserEntry
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import com.google.gson.Gson
//import androidx.room.Room
//
//
//class HistoryMoodActivity: AppCompatActivity() {
//    private lateinit var adapter: MoodEntryAdapter
//    private lateinit var database: UserEntryDatabase
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var selectedMood: Mood
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_history_mood)
//
//        database = UserEntryDatabase.getDatabase(this)
//        recyclerView = findViewById(R.id.rvMoodEntries)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        loadJournalEntries()
//
//        val journalEntryEditText = findViewById<EditText>(R.id.etJournalEntry)
//        val journalTitleEditText = findViewById<EditText>(R.id.etJournalTitle)
//        val addJournalButton = findViewById<Button>(R.id.btnAddJournal)
//        val saveJournalButton = findViewById<Button>(R.id.btnSaveJournal)
//        val cancelJournalButton = findViewById<Button>(R.id.btnCancelJournal)
//
//        intent.getStringExtra("selectedMood")?.let {
//            selectedMood = Mood.valueOf(it)
//        }
//
//        addJournalButton.setOnClickListener {
//            // Make the EditText visible when Add button is clicked
//            journalEntryEditText.visibility = View.VISIBLE
//            cancelJournalButton.visibility = View.VISIBLE
//            // Ppen the keyboard
//            journalEntryEditText.requestFocus()
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.showSoftInput(journalEntryEditText, InputMethodManager.SHOW_IMPLICIT)
//        }
//
//        cancelJournalButton.setOnClickListener{
//            journalEntryEditText.visibility = View.GONE
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(journalEntryEditText.windowToken, 0)
//            cancelJournalButton.visibility = View.GONE
//        }
//
//        saveJournalButton.setOnClickListener {
//            val journalText = journalEntryEditText.text.toString()
//            val journalTitle = journalTitleEditText.text.toString()
//            journalEntryEditText.text.clear()
//            journalEntryEditText.visibility = View.GONE
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(journalEntryEditText.windowToken, 0)
//            cancelJournalButton.visibility = View.GONE
//
//            addJournalEntry(LocalDate.now(), selectedMood, journalTitle, journalText)
//        }
//
////
////        adapter = MoodEntryAdapter(loadJournalEntries(), object : OnJournalEntryClickListener {
////            override fun onEntryClick(entry: UserEntry) {
////            }
////        })
////        findViewById<RecyclerView>(R.id.rvMoodEntries).apply {
////            layoutManager = LinearLayoutManager(this@HistoryMoodActivity)
////            this.adapter = adapter
////        }
//
//        loadJournalEntries()
//        displayTodayMood()
//    }
////
////    private fun getAllEntries(): List<UserEntry> {
////        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
////        return sharedPreferences.all.mapNotNull { (_, value) ->
////            Gson().fromJson(value as String, UserEntry::class.java)
////        }
////    }
//private fun loadJournalEntries() {
//    lifecycleScope.launch {
//        val entries = database.userEntryDao().getAllEntries()
//        adapter = MoodEntryAdapter(entries, object : OnJournalEntryClickListener )
//        recyclerView.adapter = adapter
//    }
//}
//
//
//    private fun displayTodayMood() {
//        val currentDate = LocalDate.now().toString() // Ensure the date format matches the one in your database
//        lifecycleScope.launch {
//            val todayEntry = database.userEntryDao().findEntryByDate(currentDate)
//            todayEntry?.let {
//                updateMoodDisplay(it.mood)
//            }
//        }
//    }
//
//    private fun updateMoodDisplay(mood: Mood) {
//        // Update your UI to reflect the mood
//        // This could be setting an image or text based on the mood
//        val moodEmojiImageView: ImageView = findViewById(R.id.ivMoodEmoji)
//        val moodDrawableRes = when (mood) {
//            Mood.ELATED -> R.drawable.elated_emoji
//            Mood.HAPPY -> R.drawable.happy_emoji
//            Mood.MEH -> R.drawable.meh_emoji
//            Mood.SAD -> R.drawable.sad_emoji
//            Mood.DEPRESSED -> R.drawable.depressed_emoji
//            Mood.NOT_SET -> return
//        }
//        moodEmojiImageView.setImageResource(moodDrawableRes)
//    }
//
//
//    //    private fun addJournalEntry(date: LocalDate, text: String) {
////        val entry = getEntryForDate(date) ?: UserEntry(date, Mood.NOT_SET, "")
////        entry.journalText = text
////        saveEntry(entry)
////    }
//    private fun addJournalEntry(date: LocalDate, mood: Mood, title: String, text: String) {
//        lifecycleScope.launch {
//            val entry = UserEntry(date = date, mood = mood, title = title, journalText = text)
//            database.userEntryDao().insert(entry)
//            loadJournalEntries()
//        }
//    }
//
//
////
////
////    private fun getEntryForDate(date: LocalDate): UserEntry? {
////        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
////        val entryJson = sharedPreferences.getString(date.toString(), null)
////        return entryJson?.let { Gson().fromJson(it, UserEntry::class.java) }
////    }
////    private fun saveEntry(entry: UserEntry) {
////        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
////        with(sharedPreferences.edit()) {
////            putString(entry.date.toString(), Gson().toJson(entry))
////            apply()
////        }
////    }
//}

