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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.imagefeed.ui.detail.composable.ImageDetailPage
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ImageItemUiState
import com.example.imagefeed.ui.list.states.ModalState
import com.example.imagefeed.ui.theme.ImageFeedTheme

private val DefaultSpinnerSizeDp = 48.dp
private val DefaultPaddingDp = 8.dp

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

            else -> Unit
        }
    }
}

private fun LazyListState.hasReachedEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


@Preview(showBackground = true)
@Composable
fun ImageFeedContentPreview() {
    ImageFeedTheme {
        ImageFeedContent(
            state = ImageFeedUiState(
                imageItems = List(3) { index ->
                    ImageItemUiState(
                        imageUrl = "https://www.example.com/image$index.jpg",
                        title = "Title$index",
                    )
                },
                isLoading = false,
            ),
            onImageItemClicked = {},
            onEndOfListReached = {},
            onDismissed = {})
    }
}
