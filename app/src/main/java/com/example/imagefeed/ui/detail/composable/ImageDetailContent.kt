package com.example.imagefeed.ui.detail.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.imagefeed.R
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import com.example.imagefeed.ui.theme.ImageFeedTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin

private val ImageHeightDp = 480.dp

@Composable
fun ImageDetailContent(
    uiState: ImageDetailPageUiState.Content,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = uiState.description,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
        GlideImage(
            imageModel = { uiState.imageUrl },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
            ),
            component = rememberImageComponent {
                +PlaceholderPlugin.Failure(painterResource(id = R.drawable.placeholder_image_error))
                +PlaceholderPlugin.Loading(painterResource(id = R.drawable.placeholder_image_loading))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(ImageHeightDp)
                .testTag("imageDetailContentImage"),
        )
        Text(
            text = uiState.photoId,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
        Text(
            text = uiState.photographerId,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
        Text(
            text = uiState.photographerName,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
    }
}


@Composable
fun ImageDetailLoading(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    )
}

@Preview(showBackground = true)
@Composable
fun ImageDetailContentPreview() {
    ImageFeedTheme {
        ImageDetailContent(
            uiState = ImageDetailPageUiState.Content(
                photoId = "photoId",
                photographerName = "photographerName",
                photographerId = "photographerId",
                description = "description",
                imageUrl = "test",
            ),
        )
    }
}