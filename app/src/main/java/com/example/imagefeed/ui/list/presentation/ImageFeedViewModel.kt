package com.example.imagefeed.ui.list.presentation

import androidx.lifecycle.ViewModel
import com.example.imagefeed.di.modules.CoroutineModule
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.usercase.ImageFeedCuratedPhotosUseCase
import com.example.imagefeed.ui.list.mapper.MapCuratedPhotosToImageFeedUiState
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ModalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class ImageFeedViewModel @Inject constructor(
    @Named(CoroutineModule.IO_SCOPE) private val coroutineScope: CoroutineScope,
    private val imageFeedCuratedPhotosUseCase: ImageFeedCuratedPhotosUseCase,
    private val mapCuratedPhotosToImageFeedUiState: MapCuratedPhotosToImageFeedUiState,
) : ViewModel() {

    private var fetchingJob: Job? = null

    private val _uiStateFlow = MutableStateFlow(ImageFeedUiState(isLoading = false))
    val uiStateFlow = _uiStateFlow.asStateFlow()

    init {
        fetchImages(reloadFirstPage = true)
    }

    fun dispatch(event: Event) {
        when (event) {
            is Event.OnImageItemClicked -> coroutineScope.launch {
                val imageId = imageFeedCuratedPhotosUseCase.getImageIdByIndex(event.index)
                emitState {
                    copy(
                        modalState = ModalState.Detail(imageId)
                    )
                }
            }

            is Event.OnDismissed -> emitState {
                copy(
                    modalState = ModalState.Hidden
                )
            }

            is Event.OnEndOfListReached -> {
                fetchImages(reloadFirstPage = false)
            }
        }
    }

    private fun fetchImages(reloadFirstPage: Boolean) {
        if (uiStateFlow.value.isLoading) {
            return
        }

        emitState {
            copy(
                isLoading = true,
            )
        }
        fetchingJob?.cancel()
        fetchingJob = coroutineScope.launch {
            imageFeedCuratedPhotosUseCase.queryPhotos(reloadFirstPage = reloadFirstPage)
                .also { response ->
                    emitState {
                        when (response) {
                            is Response.Success -> {
                                mapCuratedPhotosToImageFeedUiState(response.data)
                            }

                            is Response.Failure -> {
                                ImageFeedUiState(isLoading = false)
                            }
                        }
                    }
                }
        }
    }

    private fun emitState(block: ImageFeedUiState.() -> ImageFeedUiState) {
        _uiStateFlow.value = block(_uiStateFlow.value)
    }
}