package com.example.unwind.utils

import java.util.Properties

object Config {
    private val properties: Properties = Properties().apply {
        val inputStream = this::class.java.classLoader.getResourceAsStream("secret.properties")
        inputStream?.let {
            load(it)
        } ?: throw IllegalStateException("Cannot find secret.properties")
    }

    val openAiSecretKey: String by lazy {
        properties.getProperty("OPENAI_SECRET_KEY") ?: throw IllegalStateException("API key not found in secret.properties")
    }
}