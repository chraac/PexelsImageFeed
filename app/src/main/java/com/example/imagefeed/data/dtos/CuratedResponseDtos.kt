package com.example.imagefeed.data.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageSourceDto(
    @param:JsonProperty("original") val original: String,
    @param:JsonProperty("large2x") val large2x: String,
    @param:JsonProperty("large") val large: String,
    @param:JsonProperty("medium") val medium: String,
    @param:JsonProperty("small") val small: String,
    @param:JsonProperty("portrait") val portrait: String,
    @param:JsonProperty("landscape") val landscape: String,
    @param:JsonProperty("tiny") val tiny: String,
)

data class PhotoDto(
    @param:JsonProperty("id") val id: Int,
    @param:JsonProperty("width") val width: Int,
    @param:JsonProperty("height") val height: Int,
    @param:JsonProperty("url") val url: String,
    @param:JsonProperty("photographer") val photographer: String,
    @param:JsonProperty("photographer_url") val photographerUrl: String,
    @param:JsonProperty("photographer_id") val photographerId: Int,
    @param:JsonProperty("avg_color") val avgColor: String,
    @param:JsonProperty("src") val src: ImageSourceDto,
    @param:JsonProperty("liked") val liked: Boolean,
    @param:JsonProperty("alt") val alt: String,
)

data class CuratedPhotosResponseDto(
    @param:JsonProperty("page") val page: Int,
    @param:JsonProperty("per_page") val perPage: Int,
    @param:JsonProperty("photos") val photos: List<PhotoDto>,
    @param:JsonProperty("next_page") val nextPage: String,
)