package com.example.imagefeed.data.services

import com.example.imagefeed.data.dtos.CuratedPhotosResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelsServices {

    @GET("${API_VERSION}/curated")
    suspend fun getCuratedPhotos(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
    ): CuratedPhotosResponseDto

    companion object {
        private const val API_VERSION = "v1"
    }
}