package com.example.imagefeed.ui.list.presentation

import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.usercase.ImageFeedCuratedPhotosUseCase
import com.example.imagefeed.ui.list.mapper.MapCuratedPhotosToImageFeedUiState
import com.example.imagefeed.ui.list.states.ImageFeedUiState
import com.example.imagefeed.ui.list.states.ModalState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ImageFeedViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val mockImageFeedSuccessUiState = mock<ImageFeedUiState> {
        on { isLoading } doReturn false
        on { copy(isLoading = true) } doReturn mockLoadingUiState
    }
    private val mockImageFeedCuratedPhotosUseCase = mock<ImageFeedCuratedPhotosUseCase> {
        onBlocking { getImageIdByIndex(any()) } doAnswer {
            it.getArgument(0)
        }
        onBlocking { queryPhotos(any()) } doAnswer {
            Response.Success(
                if (it.getArgument(0)) {
                    mockPhotosPage1
                } else {
                    mockPhotosPage2
                }
            )
        }
    }
    private val mockMapCuratedPhotosToImageFeedUiState = mock<MapCuratedPhotosToImageFeedUiState> {
        on { invoke(mockPhotosPage1) } doReturn mockImageFeedSuccessUiState
        on { invoke(mockPhotosPage2) } doReturn mockImageFeedPage2UiState
    }

    private val subject = ImageFeedViewModel(
        coroutineScope = testScope,
        imageFeedCuratedPhotosUseCase = mockImageFeedCuratedPhotosUseCase,
        mapCuratedPhotosToImageFeedUiState = mockMapCuratedPhotosToImageFeedUiState,
    )

    @Test
    fun `when initialized then fetch images`() = runTest {
        // Then
        inOrder(mockImageFeedCuratedPhotosUseCase, mockMapCuratedPhotosToImageFeedUiState) {
            verify(mockImageFeedCuratedPhotosUseCase).queryPhotos(reloadFirstPage = true)
            verify(mockMapCuratedPhotosToImageFeedUiState).invoke(mockPhotosPage1)
        }

        subject.uiStateFlow.assertValues {
            assertEquals(mockImageFeedSuccessUiState, it.last())
        }
    }

    @Test
    fun `give fetch failed when initialized then emit error state`() = runTest {
        //Given
        whenever(mockImageFeedCuratedPhotosUseCase.queryPhotos(reloadFirstPage = true))
            .thenReturn(Response.Failure(mock()))
        val viewModel = ImageFeedViewModel(
            coroutineScope = testScope,
            imageFeedCuratedPhotosUseCase = mockImageFeedCuratedPhotosUseCase,
            mapCuratedPhotosToImageFeedUiState = mockMapCuratedPhotosToImageFeedUiState,
        )

        // Then
        viewModel.uiStateFlow.assertValues {
            assertEquals(ImageFeedUiState(isLoading = false), it.last())
        }
    }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `given event is OnImageItemClicked when dispatch then emit modal state with detail`() =
        runTest {
            // Given
            val event = Event.OnImageItemClicked(1)
            val mockImageFeedShowModalUiState = mock<ImageFeedUiState>()
            whenever(mockImageFeedSuccessUiState.copy(modalState = ModalState.Detail(1)))
                .thenReturn(mockImageFeedShowModalUiState)

            // When
            subject.dispatch(event)

            // Then
            verify(mockImageFeedSuccessUiState).copy(modalState = ModalState.Detail(1))
            subject.uiStateFlow.assertValues {
                assertEquals(mockImageFeedShowModalUiState, it.last())
            }
        }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `given event is OnDismissed when dispatch then emit modal state hidden`() =
        runTest {
            // Given
            val event = Event.OnDismissed
            val mockImageFeedHideModalUiState = mock<ImageFeedUiState>()
            whenever(mockImageFeedSuccessUiState.copy(modalState = ModalState.Hidden))
                .thenReturn(mockImageFeedHideModalUiState)

            // When
            subject.dispatch(event)

            // Then
            verify(mockImageFeedSuccessUiState).copy(modalState = ModalState.Hidden)
            subject.uiStateFlow.assertValues {
                assertEquals(mockImageFeedHideModalUiState, it.last())
            }
        }

    @Test
    fun `given event is OnEndOfListReached when dispatch then fetch images`() = runTest {
        // Given
        val event = Event.OnEndOfListReached
        clearInvocations(mockImageFeedCuratedPhotosUseCase, mockMapCuratedPhotosToImageFeedUiState)

        // When
        subject.dispatch(event)

        // Then
        inOrder(mockImageFeedCuratedPhotosUseCase, mockMapCuratedPhotosToImageFeedUiState) {
            verify(mockImageFeedCuratedPhotosUseCase).queryPhotos(reloadFirstPage = false)
            verify(mockMapCuratedPhotosToImageFeedUiState).invoke(any())
        }

        subject.uiStateFlow.assertValues {
            assertEquals(mockImageFeedPage2UiState, it.last())
        }
    }

    companion object {
        private val mockPhotosPage1 = mock<CuratedPhotos>()
        private val mockPhotosPage2 = mock<CuratedPhotos>()
        private val mockLoadingUiState = mock<ImageFeedUiState> {
            on { isLoading } doReturn true
        }
        private val mockImageFeedPage2UiState = mock<ImageFeedUiState>()

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun <T> Flow<T>.assertValues(verifier: (List<T>) -> Unit) = runTest {
            val results = mutableListOf<T>()
            backgroundScope.launch(UnconfinedTestDispatcher()) { toList(results) }
            verifier(results)
        }
    }
}
