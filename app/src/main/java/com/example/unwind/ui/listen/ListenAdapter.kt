// ListenAdapter.kt
package com.example.unwind.ui.listen
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.R
import com.example.unwind.databinding.ListenItemLayoutBinding
import com.example.unwind.ui.listen.AudioPlayerActivity
import com.example.unwind.ui.listen.ListenItem

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

        // Set click listener for each item
        binding.rectangleListenAffirmation.setOnClickListener {
            // Launch AudioPlayerActivity with corresponding track based on position
            launchAudioPlayer(R.raw.ethereal)
        }
        binding.rectangleListenCalm.setOnClickListener{
            launchAudioPlayer(R.raw.sleep)
        }
        binding.rectangleListenColor.setOnClickListener{
            launchAudioPlayer(R.raw.rain_thunder)
        }
        binding.rectangleListenNature.setOnClickListener{
            launchAudioPlayer(R.raw.bird_singing)
        }
    }

    override fun getItemCount() = dataList.size

    private fun launchAudioPlayer(resourceId: Int) {
        // Create an intent to launch AudioPlayerActivity
        val intent = Intent(context, AudioPlayerActivity::class.java).apply {
            // Pass the resource ID of the selected track as an extra
            putExtra("TRACK_RESOURCE_ID", resourceId)
        }
        // Start AudioPlayerActivity
        context.startActivity(intent)
    }
}
