package com.example.unwind.model

data class Yoga(
    val id: Int,
    val categoryName: String,
    val categoryDescription: String,
    val poses: List<YogaPose>
)

data class YogaPose(
    val id: Int,
    val english_name: String,
    val sanskrit_name_adapted: String,
    val sanskrit_name: String,
    val translation_name: String,
    val pose_description: String,
    val pose_benefits: String,
    val url_svg: String,
    val url_png: String,
    val url_svg_alt: String
)

