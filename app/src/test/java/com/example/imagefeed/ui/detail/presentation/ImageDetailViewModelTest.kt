package com.example.imagefeed.ui.detail.presentation

import com.example.imagefeed.domain.models.NoDataError
import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.usercase.ImageDetailPhotoUseCase
import com.example.imagefeed.ui.detail.mapper.MapPhotoToImageDetailPageUiState
import com.example.imagefeed.ui.detail.states.ImageDetailPageUiState
import com.example.imagefeed.ui.list.presentation.ImageFeedViewModelTest.Companion.assertValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ImageDetailViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val mockImageDetailPhotoUseCase = mock<ImageDetailPhotoUseCase>()
    private val mockMapper = mock<MapPhotoToImageDetailPageUiState>()

    private val subject = ImageDetailViewModel(
        coroutineScope = testScope,
        imageDetailPhotoUseCase = mockImageDetailPhotoUseCase,
        mapper = mockMapper,
    )

    @Test
    fun `given imageId when updateImageId called then update ui state`() = runTest {
        // Given
        val imageId = 1
        val mockPhotoResponse = mock<Photo>()
        whenever(mockImageDetailPhotoUseCase.execute(imageId)).thenReturn(
            Response.Success(
                mockPhotoResponse
            )
        )
        val mockUiState = mock<ImageDetailPageUiState.Content>()
        whenever(mockMapper(mockPhotoResponse)).thenReturn(mockUiState)

        // When
        subject.updateImageId(imageId)

        // Then
        subject.uiStateFlow.assertValues {
            assertEquals(mockUiState, it.last())
        }
    }

    @Test
    fun `given invalid imageId when updateImageId called then throw exception`() = runTest {
        // Given
        val imageId = 1
        val mockException = mock<Exception> {
            on { message } doReturn "Mock exception message"
        }
        whenever(mockImageDetailPhotoUseCase.execute(imageId)).thenReturn(
            Response.Failure(NoDataError(mockException))
        )

        // When
        subject.updateImageId(imageId)

        // Then
        subject.uiStateFlow.assertValues {
            assertEquals(
                "Mock exception message",
                (it.last() as ImageDetailPageUiState.Error).message,
            )
        }
    }
}
