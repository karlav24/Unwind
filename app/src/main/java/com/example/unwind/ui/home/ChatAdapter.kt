package com.example.unwind.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unwind.R
import com.example.unwind.model.Message

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER_MESSAGE = 1
        private const val VIEW_TYPE_GPT_RESPONSE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSentByUser) VIEW_TYPE_USER_MESSAGE else VIEW_TYPE_GPT_RESPONSE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_USER_MESSAGE) {
            UserMessageViewHolder(inflater.inflate(R.layout.item_user_message, parent, false))
        } else {
            GptMessageViewHolder(inflater.inflate(R.layout.item_gpt_response, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserMessageViewHolder) {
            holder.bind(message)
        } else if (holder is GptMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount() = messages.size

    fun updateData(newMessages: List<Message>) {
        val positionStart = messages.size
        messages.addAll(newMessages)
        notifyItemRangeInserted(positionStart, newMessages.size)
    }


    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textUserMessage)

        fun bind(message: Message) {
            textView.text = message.text
            itemView.layoutDirection = View.LAYOUT_DIRECTION_RTL // Ensures text alignment to the right
        }
    }

    class GptMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textGptMessage)

        fun bind(message: Message) {
            textView.text = message.text
            itemView.layoutDirection = View.LAYOUT_DIRECTION_LTR // Ensures text alignment to the left
        }
    }
}
