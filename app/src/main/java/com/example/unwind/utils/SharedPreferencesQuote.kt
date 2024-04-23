package com.example.unwind.utils

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import com.example.unwind.model.Quote
import com.google.gson.Gson
import java.util.Date
import java.util.Locale

class SharedPreferencesQuote (context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("quotes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveDailyQuote(quote: Quote) {
        val editor = prefs.edit()
        editor.putString("daily_quote", gson.toJson(quote))
        editor.putString("date", SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date()))
        editor.apply()
    }

    fun getDailyQuote(): Quote? {
        val quoteJson = prefs.getString("daily_quote", null) ?: return null
        return gson.fromJson(quoteJson, Quote::class.java)
    }

    fun getLastFetchDate(): String? {
        return prefs.getString("date", null)
    }
}
