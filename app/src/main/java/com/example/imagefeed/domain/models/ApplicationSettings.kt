package com.example.imagefeed.domain.models

data class ApplicationSettings(
    val apiKey: String = "",
    val perPage: Int = 10,
    val pexelsBaseUrl: String = "https://api.pexels.com/"
)
