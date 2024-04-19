package com.example.imagefeed.data.mapper

import com.example.imagefeed.data.dtos.CuratedPhotosResponseDto
import com.example.imagefeed.data.dtos.ImageSourceDto
import com.example.imagefeed.data.dtos.PhotoDto
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.ImageSource
import com.example.imagefeed.domain.models.Photo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class MapCuratedPhotosDtoToCuratedPhotosTest {
    private val mapCuratedPhotosDtoToCuratedPhotos = MapCuratedPhotosDtoToCuratedPhotos()

    @Test
    fun `given valid CuratedPhotosResponseDto, when invoke, then return CuratedPhotos`() {
        // Given
        val curatedPhotosResponseDto = CuratedPhotosResponseDto(
            page = 1,
            perPage = 10,
            nextPage = "nextPageUrl",
            photos = listOf(
                mockPhotoDto1,
                mockPhotoDto2,
            ),
        )

        // When
        val result = mapCuratedPhotosDtoToCuratedPhotos(curatedPhotosResponseDto)

        // Then
        val expected = CuratedPhotos(
            page = 1,
            photos = listOf(
                mockPhoto1,
                mockPhoto2,
            ),
        )
        assertEquals(expected, result)
    }

    companion object {
        private val mockImageSourceDto = mock<ImageSourceDto> {
            on { original } doReturn "original"
            on { large } doReturn "large"
            on { tiny } doReturn "tiny"
        }

        private val mockPhotoDto1 = mock<PhotoDto> {
            on { id } doReturn 1
            on { width } doReturn 100
            on { height } doReturn 100
            on { photographer } doReturn "photographerName1"
            on { photographerId } doReturn 11
            on { src } doReturn mockImageSourceDto
            on { alt } doReturn "alt"
        }

        private val mockPhotoDto2 = mock<PhotoDto> {
            on { id } doReturn 2
            on { width } doReturn 200
            on { height } doReturn 200
            on { photographer } doReturn "photographerName2"
            on { photographerId } doReturn 22
            on { src } doReturn mockImageSourceDto
            on { alt } doReturn "alt"
        }

        private val mockPhoto1 = Photo(
            id = 1,
            width = 100,
            height = 100,
            photographer = "photographerName1",
            photographerId = 11,
            src = ImageSource(
                original = "original",
                large = "large",
                tiny = "tiny",
            ),
            alt = "alt",
        )

        private val mockPhoto2 = Photo(
            id = 2,
            width = 200,
            height = 200,
            photographer = "photographerName2",
            photographerId = 22,
            src = ImageSource(
                original = "original",
                large = "large",
                tiny = "tiny",
            ),
            alt = "alt",
        )
    }
}