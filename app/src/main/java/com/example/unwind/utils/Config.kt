package com.example.unwind.utils

import android.content.Context
import java.util.Properties

object Config {
    private lateinit var properties: Properties

    fun initialize(context: Context) {
        properties = Properties().apply {
            val assetManager = context.assets
            val inputStream = assetManager.open("secrets.properties")
            load(inputStream)
        }
    }

    val openAiSecretKey: String by lazy {
        properties.getProperty("OPENAI_SECRET_KEY") ?: throw IllegalStateException("OPENAI_SECRET_KEY not found in secrets.properties")
    }
}
