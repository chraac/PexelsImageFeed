package com.example.imagefeed.ui.list.mapper

import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.ImageSource
import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ImageItemUiState
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class MapCuratedPhotosToImageFeedUiStateTest {
    private val subject = MapCuratedPhotosToImageFeedUiState()

    @Test
    fun `given curated photos when invoke called then map to image feed ui state`() {
        // Given
        val mockImageSrc1 = mock<ImageSource> {
            on { tiny } doReturn "tinyImage1"
        }
        val mockPhoto1 = mock<Photo> {
            on { src } doReturn mockImageSrc1
            on { photographer } doReturn "photographer1"
            on { alt } doReturn "title1"
        }
        val mockImageSrc2 = mock<ImageSource> {
            on { tiny } doReturn "tinyImage2"
        }
        val mockPhoto2 = mock<Photo> {
            on { src } doReturn mockImageSrc2
            on { photographer } doReturn "photographer2"
            on { alt } doReturn "title2"
        }
        val curatedPhotos = CuratedPhotos(
            page = 1,
            photos = listOf(
                mockPhoto1,
                mockPhoto2,
            ),
        )

        // When
        val result = subject(curatedPhotos)

        // Then
        assertEquals(
            ImageFeedUiState(
                imageItems = listOf(
                    ImageItemUiState(
                        imageUrl = "tinyImage1",
                        title = "photographer1 - title1",
                    ),
                    ImageItemUiState(
                        imageUrl = "tinyImage2",
                        title = "photographer2 - title2",
                    ),
                ),
                isLoading = false,
            ), result
        )
    }
}