package com.example.imagefeed.ui.list.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import com.example.imagefeed.ui.list.states.ImageItemUiState
import com.example.imagefeed.ui.theme.ImageFeedTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin

private val ItemPaddingDp = 8.dp
private val ImageHeightDp = 240.dp

@Composable
fun ImageItem(
    uiStata: ImageItemUiState,
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = ItemPaddingDp,
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(ItemPaddingDp)
            .clickable { onCardClicked() },
    ) {
        GlideImage(
            imageModel = { uiStata.imageUrl },
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
                .testTag("imageItemImage"),
        )
        Text(
            text = uiStata.title,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = ItemPaddingDp)
                .fillMaxWidth()
                .wrapContentHeight()
                .testTag("imageItemText"),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ImageItemPreview() {
    ImageFeedTheme {
        ImageItem(
            uiStata = ImageItemUiState(
                title = "Author - Image description",
                imageUrl = "test",
            ),
            onCardClicked = { },
        )
    }
}