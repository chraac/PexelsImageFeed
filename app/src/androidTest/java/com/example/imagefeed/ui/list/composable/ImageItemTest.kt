package com.example.imagefeed.ui.list.composable

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.imagefeed.ui.list.states.ImageItemUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImageItemTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private var clicked = false

    @Before
    fun beforeAll() {
        composeTestRule.setContent {
            ImageItem(
                uiStata = ImageItemUiState(
                    imageUrl = "https://www.example.com/image.jpg",
                    title = "Title"
                ),
                onCardClicked = { clicked = true }
            )
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun testImageItemComponentDisplayed() {
        composeTestRule.onNodeWithTag("imageItemImage", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("imageItemText", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun testImageItemComponentClicked() {
        composeTestRule.onNodeWithTag("imageItemImage", useUnmergedTree = true).performClick()
        assert(clicked)
    }
}