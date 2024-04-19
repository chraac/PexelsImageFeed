package com.example.imagefeed.domain.usercase

import com.example.imagefeed.data.repos.CuratedPhotosRepository
import com.example.imagefeed.domain.models.ApplicationSettings
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.repos.ReadApplicationSettingsRepository
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import com.example.imagefeed.domain.repos.WriteLastSucceedPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever

class ImageFeedNextPageUseCaseTest {

    private val mockReadLastSucceedPhotosRepository = mock<ReadLastSucceedPhotosRepository>()
    private val mockWriteLastSucceedPhotosRepository = mock<WriteLastSucceedPhotosRepository>()
    private val mockCuratedPhotosRepository = mock<CuratedPhotosRepository>()

    private val subject = ImageFeedNextPageUseCase(
        applicationSettingsRepository = mockApplicationSettingsRepository,
        readLastSucceedPhotosRepository = mockReadLastSucceedPhotosRepository,
        writeLastSucceedPhotosRepository = mockWriteLastSucceedPhotosRepository,
        curatedPhotosRepository = mockCuratedPhotosRepository,
    )

    @Test
    fun `given last photos page is 0 when execute then return success response with page 1`() =
        runTest {
            // Given
            val lastPhotos = CuratedPhotos(
                page = 0,
                photos = emptyList(),
            )
            val expectedPhotos = CuratedPhotos(
                page = 1,
                photos = listOf(mock()),
            )
            val expectedResponse = Response.Success<CuratedPhotos, NetworkError>(expectedPhotos)

            // When
            whenever(mockReadLastSucceedPhotosRepository.getLastPhotos()).thenReturn(lastPhotos)
            whenever(
                mockCuratedPhotosRepository.execute(
                    page = 1,
                    perPage = PER_PAGE,
                )
            ).thenReturn(
                expectedResponse
            )

            val actualResponse = subject.execute()

            // Then
            assertEquals(expectedResponse, actualResponse)
            inOrder(
                mockReadLastSucceedPhotosRepository,
                mockCuratedPhotosRepository,
                mockWriteLastSucceedPhotosRepository
            ) {
                verify(mockReadLastSucceedPhotosRepository).getLastPhotos()
                verify(mockCuratedPhotosRepository).execute(
                    page = 1,
                    perPage = PER_PAGE,
                )
                verify(mockWriteLastSucceedPhotosRepository).setLastPhotos(expectedPhotos)
            }
        }

    @Test
    fun `given last photos page is 999 and empty photos when execute then return success response with page 999`() =
        runTest {
            // Given
            val lastPhotos = CuratedPhotos(
                page = 999,
                photos = listOf(mock()),
            )
            val apiReturnedPhotos = CuratedPhotos(
                page = 1000,
                photos = emptyList(),
            )

            // When
            whenever(mockReadLastSucceedPhotosRepository.getLastPhotos()).thenReturn(lastPhotos)
            whenever(
                mockCuratedPhotosRepository.execute(
                    page = 1000,
                    perPage = PER_PAGE,
                )
            ).thenReturn(
                Response.Success(apiReturnedPhotos)
            )

            val actualResponse = subject.execute()

            // Then
            assertEquals(Response.Success<CuratedPhotos, NetworkError>(lastPhotos), actualResponse)
            inOrder(
                mockReadLastSucceedPhotosRepository,
                mockCuratedPhotosRepository,
                mockWriteLastSucceedPhotosRepository
            ) {
                verify(mockReadLastSucceedPhotosRepository).getLastPhotos()
                verify(mockCuratedPhotosRepository).execute(
                    page = 1000,
                    perPage = PER_PAGE,
                )
                verify(mockWriteLastSucceedPhotosRepository, never()).setLastPhotos(any())
            }
        }

    @Test
    fun `given exception when execute then return failure response`() =
        runTest {
            // Given
            val lastPhotos = CuratedPhotos(
                page = 0,
                photos = emptyList(),
            )
            val exception = RuntimeException()
            val expectedResponse =
                Response.Failure<CuratedPhotos, NetworkError>(NetworkError.ConnectionError(exception))

            // When
            whenever(mockReadLastSucceedPhotosRepository.getLastPhotos()).thenReturn(lastPhotos)
            whenever(
                mockCuratedPhotosRepository.execute(
                    page = 1,
                    perPage = PER_PAGE,
                )
            ).thenReturn(
                expectedResponse
            )

            val actualResponse = subject.execute()

            // Then
            assertEquals(expectedResponse, actualResponse)
            inOrder(
                mockReadLastSucceedPhotosRepository,
                mockCuratedPhotosRepository,
                mockWriteLastSucceedPhotosRepository
            ) {
                verify(mockReadLastSucceedPhotosRepository).getLastPhotos()
                verify(mockCuratedPhotosRepository).execute(
                    page = 1,
                    perPage = PER_PAGE,
                )
                verify(mockWriteLastSucceedPhotosRepository, never()).setLastPhotos(any())
            }
        }

    companion object {
        private const val PER_PAGE = 10
        private val mockSettings = ApplicationSettings(perPage = PER_PAGE)
        private val mockApplicationSettingsRepository = mock<ReadApplicationSettingsRepository> {
            onBlocking { getSetting() } doReturn mockSettings
        }
    }
}