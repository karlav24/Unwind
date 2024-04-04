package com.example.unwind.MeditationTracks
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.MeditationTracks.MeditationTrack
import com.example.unwind.R

class MeditationTrackAdapter : RecyclerView.Adapter<MeditationTrackAdapter.TrackViewHolder>() {

    private var tracks: List<MeditationTrack> = listOf()
    private var mediaPlayer: MediaPlayer? = null

    fun setTracks(tracks: List<MeditationTrack>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meditation_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val artistTextView: TextView = itemView.findViewById(R.id.textViewArtist)
        private val durationTextView: TextView = itemView.findViewById(R.id.textViewDuration)

        fun bind(track: MeditationTrack) {
            titleTextView.text = track.title
            artistTextView.text = track.artist
            val duration = track.duration
            val minutes = duration / 60
            val seconds = duration % 60
            val durationText = String.format("%02d:%02d", minutes, seconds)
            durationTextView.text = durationText

            // Add click listener for play button here if needed
            itemView.findViewById<ImageButton>(R.id.btnPlay).setOnClickListener {
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.release()
                    mediaPlayer = null
                }

                mediaPlayer = MediaPlayer.create(itemView.context, track.resourceId)
                mediaPlayer?.start()
            }
        }
    }
}
