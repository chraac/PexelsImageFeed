package com.example.imagefeed.data.repos

import com.example.imagefeed.domain.models.CuratedPhotos
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

class LastSucceedGetPhotosRepositoryTest {
    private val subject = LastSucceedGetPhotosRepository()

    @Test
    fun `given last photos page is 0, when setLastPhotos then last photos is set`() {
        // Given
        val photos = CuratedPhotos(
            page = 1,
            photos = listOf(mock(), mock()),
        )

        // When
        subject.setLastPhotos(photos)

        // Then
        assertEquals(photos, subject.getLastPhotos())
    }

    @Test
    fun `given last photos page is 1, when clear then last photos is cleared`() {
        // Given
        val photos = CuratedPhotos(
            page = 1,
            photos = listOf(mock(), mock()),
        )
        subject.setLastPhotos(photos)

        // When
        subject.clear()

        // Then
        assertEquals(CuratedPhotos(page = 0, photos = emptyList()), subject.getLastPhotos())
    }
}