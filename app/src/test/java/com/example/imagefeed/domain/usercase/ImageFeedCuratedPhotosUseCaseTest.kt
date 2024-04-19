package com.example.imagefeed.domain.usercase

import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import com.example.imagefeed.domain.repos.WriteLastSucceedPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ImageFeedCuratedPhotosUseCaseTest {
    private val mockNextPageUseCase = mock<ImageFeedNextPageUseCase> {
        onBlocking { execute() } doReturn mockNextPageResponse
    }
    private val mockWriteLastSucceedPhotosRepository = mock<WriteLastSucceedPhotosRepository>()
    private val mockReadLastSucceedPhotosRepository = mock<ReadLastSucceedPhotosRepository>()

    private val subject = ImageFeedCuratedPhotosUseCase(
        nextPageUseCase = mockNextPageUseCase,
        writeLastSucceedPhotosRepository = mockWriteLastSucceedPhotosRepository,
        readLastSucceedPhotosRepository = mockReadLastSucceedPhotosRepository,
    )

    @Test
    fun `given reloadFirstPage is true when queryPhotos then clear last photos`() =
        runTest {
            // Given
            val reloadFirstPage = true

            // When
            subject.queryPhotos(reloadFirstPage)

            // Then
            verify(mockWriteLastSucceedPhotosRepository).clear()
            verify(mockNextPageUseCase).execute()
        }

    @Test
    fun `given reloadFirstPage is false when queryPhotos then do not clear last photos`() =
        runTest {
            // Given
            val reloadFirstPage = false

            // When
            subject.queryPhotos(reloadFirstPage)

            // Then
            verify(mockWriteLastSucceedPhotosRepository, never()).clear()
            verify(mockNextPageUseCase).execute()
        }

    @Test
    fun `given index is 1 when getImageIdByIndex then return id of 2nd photo`() =
        runTest {
            // Given
            val index = 1
            val photo2 = mock<Photo> {
                on { id } doReturn 22
            }
            val lastPhotos = CuratedPhotos(
                page = 1,
                photos = listOf(mock(), photo2),
            )
            whenever(mockReadLastSucceedPhotosRepository.getLastPhotos()).thenReturn(lastPhotos)

            // When
            val actualId = subject.getImageIdByIndex(index)

            // Then
            assertEquals(22, actualId)
        }

    companion object {
        private val mockNextPageResponse = mock<Response.Success<CuratedPhotos, NetworkError>>()
    }
}