package com.example.imagefeed.ui.detail.mapper

import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import javax.inject.Inject

class MapPhotoToImageDetailPageUiState @Inject constructor() {
    operator fun invoke(photo: Photo): ImageDetailPageUiState.Content =
        ImageDetailPageUiState.Content(
            photoId = "${photo.id}",
            photographerName = photo.photographer,
            photographerId = "${photo.photographerId}",
            description = photo.alt,
            imageUrl = photo.src.original,
        )
}