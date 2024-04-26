package com.example.unwind.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.unwind.Mood
import com.example.unwind.ui.QuotesFragment
import com.example.unwind.R
import com.example.unwind.UserEntry
import com.example.unwind.ui.SettingsActivity
import com.example.unwind.databinding.FragmentHomeBinding
import com.google.gson.Gson
import java.time.LocalDate

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = activity?.intent?.getStringExtra("userName")
        binding.welcomeBac.text = if (userName != null) "Welcome back, $userName!" else "Welcome back!"

        // Initialize and attach the QuotesFragment
        val quotesFragment = QuotesFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentQuoteContainer, quotesFragment)
            .commit()

        // Setting button click to open SettingsActivity
        binding.menuVector.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.happyEmoji.setOnClickListener {  changeEmojiColor(binding.happyEmoji, Mood.HAPPY) }
        binding.sadEmoji.setOnClickListener { changeEmojiColor(binding.sadEmoji, Mood.SAD) }
        binding.elatedEmoji.setOnClickListener { changeEmojiColor(binding.elatedEmoji, Mood.ELATED) }
        binding.depressedEmoji.setOnClickListener { changeEmojiColor(binding.depressedEmoji, Mood.DEPRESSED) }
        binding.mehEmoji.setOnClickListener { changeEmojiColor(binding.mehEmoji, Mood.MEH) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding when the view is destroyed
    }

    fun onEmojiClicked(view: View) {
        val currentMood = getCurrentMood()
        val currentDate = LocalDate.now()

        // Check if a mood has already been set for today
        if (currentMood?.date == currentDate) {
            Toast.makeText(context, "You've already set your mood today!", Toast.LENGTH_SHORT).show()
            return
        }

        val mood = when (view.id) {
            R.id.happy_emoji -> Mood.HAPPY
            R.id.sad_emoji -> Mood.SAD
            R.id.elated_emoji -> Mood.ELATED
            R.id.depressed_emoji -> Mood.DEPRESSED
            R.id.meh_emoji -> Mood.MEH
            else -> Mood.NOT_SET
        }

        if (mood != Mood.NOT_SET) {
            changeEmojiColor(view as ImageView, mood)
            recordUserEntry(mood)
        }
    }

    private fun changeEmojiColor(view: ImageView, mood: Mood) {
        // Change color based on mood
        val drawableRes = when (mood) {
            Mood.ELATED -> R.drawable.elated_emoji_clicked
            Mood.HAPPY -> R.drawable.happy_emoji_clicked
            Mood.MEH -> R.drawable.meh_emoji_clicked
            Mood.SAD -> R.drawable.sad_emoji_clicked
            Mood.DEPRESSED -> R.drawable.depressed_emoji_clicked
            Mood.NOT_SET -> return
        }
        view.setImageResource(drawableRes)
    }

    private fun getCurrentMood(): UserEntry? {
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val entryJson = sharedPreferences.getString(LocalDate.now().toString(), null)
        return entryJson?.let { Gson().fromJson(it, UserEntry::class.java) }
    }
    private fun recordUserEntry(mood: Mood) {
        val entry = UserEntry(LocalDate.now(), mood)
        saveEntry(entry)
    }
    private fun saveEntry(entry: UserEntry) {
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(entry.date.toString(), Gson().toJson(entry))
            apply()
        }
    }


}

