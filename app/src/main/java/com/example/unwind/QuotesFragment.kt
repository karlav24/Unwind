package com.example.unwind

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.unwind.R

class QuotesFragment : Fragment() {

    private lateinit var textViewQuote: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quotes, container, false)

        textViewQuote = view.findViewById(R.id.textViewQuote)

        loadQuote()

        return view
    }

    private fun loadQuote() {
        val sampleQuote = "In the middle of every difficulty lies opportunity. - Albert Einstein"
        textViewQuote.text = sampleQuote
        textViewQuote.setTextColor(Color.WHITE)

    }
}
