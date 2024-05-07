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

//
//import UserEntryDatabase
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import com.example.unwind.user.journal.HistoryMoodActivity
//import com.example.unwind.ui.QuotesFragment
//import com.example.unwind.R
//import com.example.unwind.ui.SettingsActivity
//import com.example.unwind.databinding.FragmentHomeBinding
//import com.example.unwind.model.Mood
//import com.example.unwind.model.UserEntry
//import com.google.gson.Gson
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//
//class HomeFragment : Fragment() {
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//    private var selectedMood: Mood? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val userName = activity?.intent?.getStringExtra("userName")
//        binding.welcomeBac.text = if (userName != null) "Welcome back, $userName!" else "Welcome back!"
//
//        // Initialize and attach the QuotesFragment
//        val quotesFragment = QuotesFragment()
//        childFragmentManager.beginTransaction()
//            .replace(R.id.fragmentQuoteContainer, quotesFragment)
//            .commit()
//
//        // Setting button click to open SettingsActivity
//        binding.menuVector.setOnClickListener {
//            val intent = Intent(activity, SettingsActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.journal.setOnClickListener {
//            val intent = Intent(activity, HistoryMoodActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.breatheImgButton.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
//        }
//
//        binding.listenImgButton.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_home_to_navigation_notifications)
//        }
//
//        binding.journal.setOnClickListener {
//            val intent = Intent(activity, HistoryMoodActivity::class.java)
//            intent.putExtra("mood", selectedMood.toString())
//            startActivity(intent)
//        }
//
////        binding.happyEmoji.setOnClickListener {  changeEmojiColor(binding.happyEmoji, Mood.HAPPY) }
////        binding.sadEmoji.setOnClickListener { changeEmojiColor(binding.sadEmoji, Mood.SAD) }
////        binding.elatedEmoji.setOnClickListener { changeEmojiColor(binding.elatedEmoji, Mood.ELATED) }
////        binding.depressedEmoji.setOnClickListener { changeEmojiColor(binding.depressedEmoji, Mood.DEPRESSED) }
////        binding.mehEmoji.setOnClickListener { changeEmojiColor(binding.mehEmoji, Mood.MEH) }

//        binding.happyEmoji.setOnClickListener { selectMood(Mood.HAPPY) }
//        binding.sadEmoji.setOnClickListener { selectMood(Mood.SAD) }
//        binding.elatedEmoji.setOnClickListener { selectMood(Mood.ELATED) }
//        binding.depressedEmoji.setOnClickListener { selectMood(Mood.DEPRESSED) }
//        binding.mehEmoji.setOnClickListener { selectMood(Mood.MEH) }
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null // Clean up the binding when the view is destroyed
//    }
////    fun onEmojiClicked(view: View) {
////        val currentMood = getCurrentMood()
////        val currentDate = LocalDate.now()
////
////        // Check if a mood has already been set for today
////        if (currentMood!=null && currentMood.date == currentDate) {
////            Toast.makeText(context, "You've already set your mood today!", Toast.LENGTH_SHORT).show()
////            return
////        }
////
////        val mood = when (view.id) {
////            R.id.happy_emoji -> Mood.HAPPY
////            R.id.sad_emoji -> Mood.SAD
////            R.id.elated_emoji -> Mood.ELATED
////            R.id.depressed_emoji -> Mood.DEPRESSED
////            R.id.meh_emoji -> Mood.MEH
////            else -> Mood.NOT_SET
////        }
////
////        if (mood != Mood.NOT_SET) {
////            changeEmojiColor(view as ImageView, mood)
////            recordUserEntry(mood)
////        }
////    }
//
//    private fun selectMood(mood: Mood) {
//        val currentDate = LocalDate.now()
//        val currentMood = getCurrentMood()
//
//        if (currentMood != null && currentMood.date == currentDate) {
//            Toast.makeText(context, "You've already set your mood today!", Toast.LENGTH_SHORT).show()
//        } else {
//            selectedMood = mood
//            recordUserEntry(mood)
//            updateMoodDisplay(view, mood)
//        }
//    }
////    private fun updateMoodDisplay(mood: Mood) {
////        val moodDrawableRes = when (mood) {
////            Mood.ELATED -> R.drawable.elated_emoji_clicked
////            Mood.HAPPY -> R.drawable.happy_emoji_clicked
////            Mood.MEH -> R.drawable.meh_emoji_clicked
////            Mood.SAD -> R.drawable.sad_emoji_clicked
////            Mood.DEPRESSED -> R.drawable.depressed_emoji_clicked
////            Mood.NOT_SET -> return
////        }
////        binding.ivMoodEmoji.setImageResource(moodDrawableRes)
////    }
//
//    private fun updateMoodDisplay(mood: Mood) {
//        val moodDrawableRes = getMoodDrawable(mood)
//        if (moodDrawableRes != 0) {
//            binding.ivMoodEmoji.setImageResource(moodDrawableRes)
//            binding.ivMoodEmoji.visibility = View.VISIBLE
//        } else {
//            binding.ivMoodEmoji.visibility = View.GONE // Hide the ImageView if no mood is set
//        }
//    }
//
//    private fun getMoodDrawable(mood: Mood): Int {
//        return when (mood) {
//            Mood.ELATED -> R.drawable.elated_emoji
//            Mood.HAPPY -> R.drawable.happy_emoji
//            Mood.MEH -> R.drawable.meh_emoji
//            Mood.SAD -> R.drawable.sad_emoji
//            Mood.DEPRESSED -> R.drawable.depressed_emoji
//            Mood.NOT_SET -> 0
//        }
//    }
//
////    private fun changeEmojiColor(view: ImageView, mood: Mood) {
////        // Change color based on mood
////        val drawableRes = when (mood) {
////            Mood.ELATED -> R.drawable.elated_emoji_clicked
////            Mood.HAPPY -> R.drawable.happy_emoji_clicked
////            Mood.MEH -> R.drawable.meh_emoji_clicked
////            Mood.SAD -> R.drawable.sad_emoji_clicked
////            Mood.DEPRESSED -> R.drawable.depressed_emoji_clicked
////            Mood.NOT_SET -> return
////        }
////        view.setImageResource(drawableRes)
////    }
//
//
//
//
//    private fun getCurrentMood(): UserEntry? {
//        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
//        val entryJson = sharedPreferences.getString(LocalDate.now().toString(), null)
//        return entryJson?.let { Gson().fromJson(it, UserEntry::class.java) }
//    }
//    private fun recordUserEntry(mood: Mood) {
//        val entry = UserEntry(date = LocalDate.now(), mood = mood)
//        saveEntry(entry)
//    }
//
//    private fun saveEntry(entry: UserEntry) {
//        lifecycleScope.launch {
//            val userDao = UserEntryDatabase.getDatabase(requireContext()).userEntryDao()
//            userDao.insert(entry)
//        }
//    }
//
//
////    private fun saveEntry(entry: UserEntry) {
////        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
////        with(sharedPreferences.edit()) {
////            putString(entry.date.toString(), Gson().toJson(entry))
////            apply()
////        }
////    }
//
//
//}


