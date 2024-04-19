package com.example.imagefeed.ui.list.mapper

import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ImageItemUiState
import javax.inject.Inject

class MapCuratedPhotosToImageFeedUiState @Inject constructor() {
    operator fun invoke(from: CuratedPhotos) = ImageFeedUiState(
        imageItems = from.photos.map {
            ImageItemUiState(
                imageUrl = it.src.tiny,
                title = "${it.photographer} - ${it.alt}",
            )
        },
        isLoading = false,
    )
}