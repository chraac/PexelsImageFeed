package com.example.imagefeed.domain.models


data class ImageSource(
    val original: String,
    val large: String,
    val tiny: String,
)

data class Photo(
    val id: Int,
    val width: Int,
    val height: Int,
    val photographer: String,
    val photographerId: Int,
    val src: ImageSource,
    val alt: String,
)

data class CuratedPhotos(
    val page: Int = 0,
    val photos: List<Photo> = emptyList(),
)
