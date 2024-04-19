package com.example.imagefeed.ui.detail.presentation

import androidx.lifecycle.ViewModel
import com.example.imagefeed.di.modules.CoroutineModule
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.usercase.ImageDetailPhotoUseCase
import com.example.imagefeed.ui.detail.mapper.MapPhotoToImageDetailPageUiState
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class ImageDetailViewModel @Inject constructor(
    @Named(CoroutineModule.IO_SCOPE) private val coroutineScope: CoroutineScope,
    private val imageDetailPhotoUseCase: ImageDetailPhotoUseCase,
    private val mapper: MapPhotoToImageDetailPageUiState,
) : ViewModel() {
    private val _uiStateFlow =
        MutableStateFlow<ImageDetailPageUiState>(ImageDetailPageUiState.Loading)
    val uiStateFlow = _uiStateFlow.asStateFlow()

    fun updateImageId(imageId: Int) {
        coroutineScope.launch {
            _uiStateFlow.value = imageDetailPhotoUseCase.execute(imageId).let {
                when (it) {
                    is Response.Success -> mapper(it.data)
                    is Response.Failure -> throw IllegalStateException(it.error.exception)
                }
            }
        }
    }
}