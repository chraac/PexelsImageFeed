package com.example.imagefeed.ui.list.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagefeed.ImageFeedApplication
import com.example.imagefeed.di.subcomponents.ImageFeedSubComponent
import com.example.imagefeed.ui.detail.composable.ImageDetailPage
import com.example.imagefeed.ui.list.presentation.Event
import com.example.imagefeed.ui.list.presentation.ImageFeedViewModel
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ModalState

private val DefaultSpinnerSizeDp = 48.dp
private val DefaultPaddingDp = 8.dp

@Composable
fun ImageFeedHost(
    modifier: Modifier = Modifier,
) {
    val viewModel = buildActivityViewModel<ImageFeedViewModel>()
    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    ImageFeedContent(
        state = state,
        onImageItemClicked = { viewModel.dispatch(Event.OnImageItemClicked(it)) },
        onEndOfListReached = { viewModel.dispatch(Event.OnEndOfListReached) },
        onDismissed = { viewModel.dispatch(Event.OnDismissed) },
        modifier = modifier,
    )
}

@Composable
fun ImageFeedContent(
    state: ImageFeedUiState,
    onImageItemClicked: (Int) -> Unit,
    onEndOfListReached: () -> Unit,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val isAtBottom by remember {
        derivedStateOf {
            scrollState.hasReachedEnd()
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            onEndOfListReached()
        }
    }

    LazyColumn(
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(state.imageItems.size) { index ->
            ImageItem(
                uiStata = state.imageItems[index],
                onCardClicked = { onImageItemClicked(index) },
                modifier = Modifier.testTag("imageItem$index"),
            )
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(DefaultSpinnerSizeDp)
                        .padding(DefaultPaddingDp),
                )
            }
        }
    }

    with(state) {
        when (modalState) {
            is ModalState.Detail -> ImageDetailPage(
                imageId = modalState.imageId,
                onDismissRequest = onDismissed,
            )

            else -> kotlin.Unit
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun buildImageFeedSubComponent(): ImageFeedSubComponent {
    val appContext = LocalContext.current.applicationContext as ImageFeedApplication
    return viewModel(
        modelClass = ImageFeedSubComponent::class.java,
        viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
        factory = object : ViewModelProvider.Factory {
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                return appContext
                    .appComponent
                    .imageFeedSubComponentBuilder
                    .build() as VM
            }
        },
    )
}


@Composable
inline fun <reified T : ViewModel> buildActivityViewModel(): T {
    val subComponent = buildImageFeedSubComponent()
    return viewModel(
        modelClass = T::class.java,
        viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
        factory = object : ViewModelProvider.Factory {
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                return subComponent
                    .viewModelFactory()
                    .create(modelClass)
            }
        },
    )
}

private fun LazyListState.hasReachedEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1