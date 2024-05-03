// ListenAdapter.kt
package com.example.unwind.ui.listen
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.R
import com.example.unwind.databinding.ListenItemLayoutBinding

class ListenAdapter(private val dataList: List<ListenItem>) : RecyclerView.Adapter<ListenAdapter.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(val binding: ListenItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListenItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        val binding = holder.binding
        val genres = listOf("Affirmation", "Calm", "Color", "Nature")
        binding.rectangleListenAffirmation.setOnClickListener {
            // Launch AudioPlayerActivity with corresponding track based on genre
            launchAudioPlayer(genres[0])
        }
        binding.rectangleListenCalm.setOnClickListener{
            launchAudioPlayer(genres[1])
        }
        binding.rectangleListenColor.setOnClickListener{
            launchAudioPlayer(genres[2])
        }
        binding.rectangleListenNature.setOnClickListener{
            launchAudioPlayer(genres[3])
        }
    }

    override fun getItemCount() = dataList.size

    private fun launchAudioPlayer(genre: String) {
        // Create an intent to launch AudioPlayerActivity
        val intent = Intent(context, AudioPlayerActivity::class.java).apply {
            // Pass the resource ID of the selected track as an extra
            putExtra("SELECTED_GENRE", genre)
        }
        // Start AudioPlayerActivity
        context.startActivity(intent)
    }
}
