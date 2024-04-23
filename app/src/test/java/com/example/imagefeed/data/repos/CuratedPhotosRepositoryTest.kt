package com.example.imagefeed.data.repos

import com.example.imagefeed.data.dtos.CuratedPhotosResponseDto
import com.example.imagefeed.data.mapper.MapCuratedPhotosDtoToCuratedPhotos
import com.example.imagefeed.data.services.PexelsServices
import com.example.imagefeed.domain.mapper.NetworkResponseMapper
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock

class CuratedPhotosRepositoryTest {
    private val mockNetworkResponseMapper = NetworkResponseMapper()
    private val mockPexelsServices = mock<PexelsServices> {
        onBlocking { getCuratedPhotos(PAGE, PER_PAGE) } doReturn mockResponseDto
    }
    private val mockMapCuratedPhotosDtoToCuratedPhotos = mock<MapCuratedPhotosDtoToCuratedPhotos> {
        on { invoke(mockResponseDto) } doReturn mockCuratedPhotos
    }

    private val subject = CuratedPhotosRepository(
        networkResponseMapper = mockNetworkResponseMapper,
        pexelsServices = mockPexelsServices,
        mapCuratedPhotosDtoToCuratedPhotos = mockMapCuratedPhotosDtoToCuratedPhotos,
    )

    @Test
    fun `given page and perPage when execute then return response`() = runTest {
        // When
        val result = subject.execute(PAGE, PER_PAGE)

        // Then
        assertEquals(Response.Success<CuratedPhotos, NetworkError>(mockCuratedPhotos), result)
        inOrder(mockPexelsServices, mockMapCuratedPhotosDtoToCuratedPhotos) {
            verify(mockPexelsServices).getCuratedPhotos(PAGE, PER_PAGE)
            verify(mockMapCuratedPhotosDtoToCuratedPhotos).invoke(mockResponseDto)
        }
    }

    companion object {
        private const val PAGE = 1
        private const val PER_PAGE = 10
        private val mockResponseDto = mock<CuratedPhotosResponseDto>()
        private val mockCuratedPhotos = mock<CuratedPhotos>()
    }
}