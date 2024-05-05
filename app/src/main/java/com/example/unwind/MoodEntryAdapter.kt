package com.example.unwind

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoodEntryAdapter(
    private val entries: List<UserEntry>,
    private val clickListener: OnJournalEntryClickListener
) :
    RecyclerView.Adapter<MoodEntryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount() = entries.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moodEmoji: ImageView = itemView.findViewById(R.id.ivMoodEmoji)
        private val moodDate: TextView = itemView.findViewById(R.id.tvMoodDate)
        private val journalPreview: TextView = itemView.findViewById(R.id.tvJournalPreview)

        fun bind(entry: UserEntry) {
            // Set mood emoji here if needed
            moodDate.text = entry.date.toString()
            journalPreview.text = entry.journalText

            itemView.setOnClickListener {
                clickListener.onEntryClick(entry)
            }
        }
    }
}

interface OnJournalEntryClickListener {
    fun onEntryClick(entry: UserEntry)
}
