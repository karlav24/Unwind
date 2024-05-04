package com.example.unwind

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MoodEntryAdapter(private val entries: List<UserEntry>,
                       private val clickListener: OnJournalEntryClickListener) :
    RecyclerView.Adapter<MoodEntryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moodEmoji: ImageView = view.findViewById(R.id.ivMoodEmoji)
        val moodDate: TextView = view.findViewById(R.id.tvMoodDate)
        val journalPreview: TextView = view.findViewById(R.id.tvJournalPreview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        // holder.moodEmoji.setImageResource(...)
        holder.moodDate.text = entry.date.toString()
        holder.journalPreview.text = entry.journalText
    }

    override fun getItemCount() = entries.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(entry: UserEntry) {
            // Assume you have a TextView in your item_mood_entry.xml to show some text
            itemView.findViewById<TextView>(R.id.tvJournalPreview).text = entry.journalText
            itemView.setOnClickListener {
                clickListener.onEntryClick(entry)
            }
        }
    }
}

interface OnJournalEntryClickListener {
    fun onEntryClick(entry: UserEntry)
}

