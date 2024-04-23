package com.example.unwind.ui

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.unwind.R
import com.example.unwind.network.QuoteApiRetrofit
import com.example.unwind.model.Quote
import com.example.unwind.utils.SharedPreferencesQuote
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.util.Locale

class QuotesFragment : Fragment() {
    private lateinit var textViewQuote: TextView
    private lateinit var sharedPreferencesHelper: SharedPreferencesQuote

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_quotes, container, false)
            textViewQuote = view.findViewById(R.id.textViewQuote)
            sharedPreferencesHelper = SharedPreferencesQuote(requireContext())
            loadQuote()
            return view
        }

        private fun loadQuote() {
            val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            val lastFetchDate = sharedPreferencesHelper.getLastFetchDate()

            if (currentDate != lastFetchDate) {
                QuoteApiRetrofit.instance.getRandomQuote().enqueue(object : Callback<Quote> {
                    override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                        response.body()?.let {
                            textViewQuote.text = "\"${it.content}\" - ${it.author}"
                            textViewQuote.setTextColor(Color.WHITE)
                            sharedPreferencesHelper.saveDailyQuote(it)
                        }
                    }

                    override fun onFailure(call: Call<Quote>, t: Throwable) {
                        textViewQuote.text = "Failed to load new quote. Check your network connection."
                    }
                })
            } else {
                sharedPreferencesHelper.getDailyQuote()?.let {
                    textViewQuote.text = "\"${it.content}\" - ${it.author}"
                    textViewQuote.setTextColor(Color.WHITE)
                }
            }
        }
    }

