package com.example.unwind.model
data class Quote(
    val _id: String,         // Unique identifier for the quote
    val content: String,     // The quotation text
    val author: String,      // The full name of the author
    val authorSlug: String,  // The 'slug' of the quote author
    val length: Int,         // The length of the quote (number of characters)
    val tags: List<String>   // An array of tag names for this quote
)
