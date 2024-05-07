package com.example.unwind.user.journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.R
import com.example.unwind.model.Mood
import com.example.unwind.model.UserEntry
//
//class MoodEntryAdapter(
//    private val entries: List<UserEntry>,
//    private val clickListener: OnJournalEntryClickListener
//) :
//    RecyclerView.Adapter<MoodEntryAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_mood_entry, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val entry = entries[position]
//        holder.bind(entry)
//    }
//
//    override fun getItemCount() = entries.size
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val moodEmoji: ImageView = itemView.findViewById(R.id.ivMoodEmoji)
//        private val moodDate: TextView = itemView.findViewById(R.id.tvMoodDate)
//        private val journalPreview: TextView = itemView.findViewById(R.id.tvJournalPreview)
//
//        fun bind(entry: UserEntry) {
//            // Set mood emoji here if needed
//            moodDate.text = entry.date.toString()
//            journalPreview.text = entry.journalText
//
//            itemView.setOnClickListener {
//                clickListener.onEntryClick(entry)
//            }
//        }
//    }
//}
//
//interface OnJournalEntryClickListener {
//    fun onEntryClick(entry: UserEntry)
//}

class MoodEntryAdapter(private val entries: List<UserEntry>, private val clickListener: (UserEntry) -> Unit) :
    RecyclerView.Adapter<MoodEntryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moodEmojiImageView: ImageView = itemView.findViewById(R.id.ivMoodEmoji)
        private val entryDateTextView: TextView = itemView.findViewById(R.id.tvMoodDate)
        private val entryTextView: TextView = itemView.findViewById(R.id.tvJournalPreview)

        fun bind(entry: UserEntry, clickListener: (UserEntry) -> Unit) {
            moodEmojiImageView.setImageResource(getMoodDrawable(entry.mood))
            entryDateTextView.text = entry.date.toString()
            entryTextView.text = entry.journalText
            itemView.setOnClickListener { clickListener(entry) }
        }

        private fun getMoodDrawable(mood: Mood): Int {
            return when (mood) {
                Mood.ELATED -> R.drawable.elated_emoji
                Mood.HAPPY -> R.drawable.happy_emoji
                Mood.MEH -> R.drawable.meh_emoji
                Mood.SAD -> R.drawable.sad_emoji
                Mood.DEPRESSED -> R.drawable.depressed_emoji
                Mood.NOT_SET -> 0
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mood_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(entries[position], clickListener)
    }

    override fun getItemCount() = entries.size
}




