package com.example.unwind.ui.home

import UserEntryDatabase
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.unwind.R
import com.example.unwind.databinding.FragmentHomeBinding
import com.example.unwind.model.Mood
import com.example.unwind.model.UserEntry
import com.example.unwind.ui.QuotesFragment
import com.example.unwind.ui.SettingsActivity
import com.example.unwind.user.journal.HistoryMoodActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.example.unwind.ui.ChatActivity

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null

    private var selectedMood: Mood? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupMoodButtons()
        val quotesFragment = QuotesFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentQuoteContainer, quotesFragment)
            .commit()
    }

    private fun setupViews() {
        binding?.apply {
            welcomeBac.text = activity?.intent?.getStringExtra("userName")?.let { "Welcome back, $it!" } ?: "Welcome back!"

            journal.setOnClickListener {
                val intent = Intent(activity, HistoryMoodActivity::class.java).apply {
                    putExtra("mood", selectedMood?.name)
                }
                startActivity(intent)
            }

            menuVector.setOnClickListener {
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
            }


            breatheImgButton.setOnClickListener { findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard) }
            listenImgButton.setOnClickListener { findNavController().navigate(R.id.action_navigation_home_to_navigation_notifications) }

            // Add GPT button listener here
            gptButton.setOnClickListener {
                val intent = Intent(activity, ChatActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupMoodButtons() {
        binding?.apply {
            happyEmoji.setOnClickListener { selectMood(Mood.HAPPY) }
            sadEmoji.setOnClickListener { selectMood(Mood.SAD) }
            elatedEmoji.setOnClickListener { selectMood(Mood.ELATED) }
            depressedEmoji.setOnClickListener { selectMood(Mood.DEPRESSED) }
            mehEmoji.setOnClickListener { selectMood(Mood.MEH) }
        }
    }

    private fun selectMood(mood: Mood) {
        val currentDate = LocalDate.now()
        val currentMood = getCurrentMood()

        if (currentMood != null && currentMood.date == currentDate) {
            Toast.makeText(context, "You've already set your mood today!", Toast.LENGTH_SHORT).show()
        } else {
            selectedMood = mood
            recordUserEntry(mood)
        }
    }

    private fun getCurrentMood(): UserEntry? {
        return context?.let {
            val sharedPreferences = it.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val entryJson = sharedPreferences.getString(LocalDate.now().toString(), null)
            Gson().fromJson(entryJson, UserEntry::class.java)
        }
    }

    private fun recordUserEntry(mood: Mood) {
        val entry = UserEntry(date = LocalDate.now(), mood = mood)
        saveEntry(entry)
    }

    private fun saveEntry(entry: UserEntry) {
        lifecycleScope.launch {
            UserEntryDatabase.getDatabase(requireContext()).userEntryDao().insert(entry)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}