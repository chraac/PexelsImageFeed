package com.example.imagefeed.domain.usercase

import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NoDataError
import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ImageDetailPhotoUseCaseTest {
    private val mockReadLastSucceedPhotosRepository = mock<ReadLastSucceedPhotosRepository>()
    private val subject = ImageDetailPhotoUseCase(
        readLastSucceedPhotosRepository = mockReadLastSucceedPhotosRepository,
    )

    @Test
    fun `given valid imageId when execute then return success response with photo`() = runTest {
        // Given
        val imageId = 2
        whenever(mockReadLastSucceedPhotosRepository.getLastPhotos()).thenReturn(mockPhotos)

        // When
        val actualResponse = subject.execute(imageId)

        // Then
        assertEquals(Response.Success<Photo, NoDataError>(mockPhoto2), actualResponse)
    }

    @Test
    fun `given invalid imageId when execute then return failure response with NoDataError`() =
        runTest {
            // Given
            val imageId = 4
            whenever(mockReadLastSucceedPhotosRepository.getLastPhotos()).thenReturn(mockPhotos)

            // When
            val actualResponse = subject.execute(imageId)

            // Then
            assertEquals(
                "No photo with id $imageId",
                (actualResponse as Response.Failure<Photo, NoDataError>).error.exception.message
            )
        }

    companion object {
        private val mockPhoto1 = mock<Photo> {
            on { id } doReturn 1
        }
        private val mockPhoto2 = mock<Photo> {
            on { id } doReturn 2
        }
        private val mockPhoto3 = mock<Photo> {
            on { id } doReturn 3
        }
        private val mockPhotos = CuratedPhotos(
            page = 1,
            photos = listOf(
                mockPhoto3,
                mockPhoto2,
                mockPhoto1,
            ),
        )
    }
}