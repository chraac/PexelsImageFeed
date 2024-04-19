package com.example.imagefeed.domain.usercase

import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import com.example.imagefeed.domain.repos.WriteLastSucceedPhotosRepository
import javax.inject.Inject

class ImageFeedCuratedPhotosUseCase @Inject constructor(
    private val nextPageUseCase: ImageFeedNextPageUseCase,
    private val writeLastSucceedPhotosRepository: WriteLastSucceedPhotosRepository,
    private val readLastSucceedPhotosRepository: ReadLastSucceedPhotosRepository,
) {
    suspend fun queryPhotos(reloadFirstPage: Boolean): Response<CuratedPhotos, NetworkError> {
        if (reloadFirstPage) {
            writeLastSucceedPhotosRepository.clear()
        }

        return nextPageUseCase.execute()
    }

    @Suppress("RedundantSuspendModifier")
    suspend fun getImageIdByIndex(index: Int): Int =
        readLastSucceedPhotosRepository.getLastPhotos().photos[index].id
}