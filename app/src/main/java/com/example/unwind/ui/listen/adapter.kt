// ListenAdapter.kt
package com.example.unwind.ui.listen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.databinding.ListenItemLayoutBinding

class ListenAdapter(private val dataList: List<ListenItem>) : RecyclerView.Adapter<ListenAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListenItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListenItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        //holder.binding.textView.text = item.text
    }

    override fun getItemCount() = dataList.size
}
