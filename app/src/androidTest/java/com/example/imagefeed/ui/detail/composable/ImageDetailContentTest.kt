package com.example.imagefeed.ui.detail.composable

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImageDetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun beforeAll() {
        composeTestRule.setContent {
            ImageDetailContent(
                uiState = ImageDetailPageUiState.Content(
                    imageUrl = "https://www.example.com/image.jpg",
                    description = "Description",
                    photoId = "photoId",
                    photographerId = "photographerId",
                    photographerName = "photographerName",
                ),
            )
        }
    }

    @Test
    fun testImageDetailContentComponentDisplayed() {
        composeTestRule.onNodeWithTag("imageDetailContentImage", useUnmergedTree = true)
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Description", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("photoId", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("photographerId", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("photographerName", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}