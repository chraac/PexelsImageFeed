package com.example.imagefeed.ui.list.states

data class ImageFeedUiState(
    val imageItems: List<ImageItemUiState> = emptyList(),
    val isLoading: Boolean = true,
    val modalState: ModalState = ModalState.Hidden,
)

sealed interface ModalState {
    data object Hidden : ModalState
    data class Detail(val imageId: Int) : ModalState
}
