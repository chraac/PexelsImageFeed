package com.example.imagefeed.ui.detail.states


sealed interface ImageDetailPageUiState {

    data object Loading : ImageDetailPageUiState

    data class Content(
        val photoId: String,
        val photographerName: String,
        val photographerId: String,
        val description: String,
        val imageUrl: String,
    ) : ImageDetailPageUiState
}
