package com.example.imagefeed.ui.list.composable

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ImageItemUiState
import org.junit.Rule
import org.junit.Test

class ImageFeedHostTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private var imageItemClickedIndex = -1

    @Test
    fun testImageFeedContentComponentDisplayed() {
        showImageFeed(imageFeedUiStateWith2Images)
        composeTestRule.onNodeWithTag("imageItem0", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("imageItem1", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun testImageFeedContentComponentClicked() {
        showImageFeed(imageFeedUiStateWith3Images)
        composeTestRule.onNodeWithTag("imageItem1", useUnmergedTree = true).performClick()
        assert(imageItemClickedIndex == 1)
    }

    private fun showImageFeed(uiState: ImageFeedUiState) {
        composeTestRule.setContent {
            ImageFeedContent(
                state = uiState,
                onImageItemClicked = { imageItemClickedIndex = it },
                onEndOfListReached = { },
                onDismissed = {},
                modifier = Modifier.testTag(IMAGE_CONTENT_TAG),
            )
        }
        composeTestRule.waitForIdle()
    }

    companion object {
        private const val IMAGE_CONTENT_TAG = "imageContent"

        private val imageItems = List(3) { index ->
            ImageItemUiState(
                imageUrl = "https://www.example.com/image$index.jpg",
                title = "Title$index",
            )
        }

        private val imageFeedUiStateWith2Images = ImageFeedUiState(
            imageItems = imageItems.subList(0, 2),
            isLoading = false,
        )

        private val imageFeedUiStateWith3Images = ImageFeedUiState(
            imageItems = imageItems,
            isLoading = false,
        )
    }
}