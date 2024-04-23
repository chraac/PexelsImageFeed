package com.example.imagefeed.ui.list.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagefeed.ImageFeedApplication
import com.example.imagefeed.di.subcomponents.ImageFeedSubComponent
import com.example.imagefeed.ui.list.presentation.Event
import com.example.imagefeed.ui.list.presentation.ImageFeedViewModel


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