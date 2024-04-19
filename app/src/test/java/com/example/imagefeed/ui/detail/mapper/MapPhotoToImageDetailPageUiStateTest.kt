package com.example.imagefeed.ui.detail.mapper

import com.example.imagefeed.domain.models.ImageSource
import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class MapPhotoToImageDetailPageUiStateTest {
    private val subject = MapPhotoToImageDetailPageUiState()

    @Test
    fun `given photo when invoke called then map to image detail page ui state`() {
        // Given
        val imageSource = mock<ImageSource> {
            on { original } doReturn "originalImage"
        }
        val photo = mock<Photo> {
            on { id } doReturn 1
            on { photographer } doReturn "photographer"
            on { photographerId } doReturn 11
            on { alt } doReturn "title"
            on { src } doReturn imageSource
        }

        // When
        val result = subject.invoke(photo)

        // Then
        assertEquals(
            ImageDetailPageUiState.Content(
                photoId = "1",
                photographerName = "photographer",
                photographerId = "11",
                description = "title",
                imageUrl = "originalImage",
            ), result
        )
    }
}