package com.example.imagefeed.ui.detail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.imagefeed.ui.detail.presentation.ImageDetailViewModel
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import com.example.imagefeed.ui.list.composable.buildActivityViewModel

private val ContentPaddingDp = 8.dp


@Composable
fun ImageDetailPage(
    imageId: Int,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = buildImageDetailViewModel(imageId)
    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Dialog(
        properties = DialogProperties(
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = false,
        ),
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Box(
                modifier = Modifier
                    .padding(ContentPaddingDp),
            ) {
                when (val lastState = state) {
                    is ImageDetailPageUiState.Loading -> {
                        ImageDetailLoading(
                            modifier = modifier,
                        )
                    }

                    is ImageDetailPageUiState.Content -> {
                        ImageDetailContent(
                            uiState = lastState,
                            modifier = modifier,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun buildImageDetailViewModel(imageId: Int): ImageDetailViewModel {
    val viewModel = buildActivityViewModel<ImageDetailViewModel>()
    viewModel.updateImageId(imageId)
    return viewModel
}
