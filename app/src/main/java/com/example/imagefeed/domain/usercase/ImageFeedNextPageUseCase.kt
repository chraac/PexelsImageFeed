package com.example.imagefeed.domain.usercase

import com.example.imagefeed.data.repos.CuratedPhotosRepository
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.repos.ReadApplicationSettingsRepository
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import com.example.imagefeed.domain.repos.WriteLastSucceedPhotosRepository
import javax.inject.Inject

class ImageFeedNextPageUseCase @Inject constructor(
    applicationSettingsRepository: ReadApplicationSettingsRepository,
    private val readLastSucceedPhotosRepository: ReadLastSucceedPhotosRepository,
    private val writeLastSucceedPhotosRepository: WriteLastSucceedPhotosRepository,
    private val curatedPhotosRepository: CuratedPhotosRepository,
) {
    private val perPage = applicationSettingsRepository.getSetting().perPage

    suspend fun execute(): Response<CuratedPhotos, NetworkError> {
        val lastPhotos = readLastSucceedPhotosRepository.getLastPhotos()
        val currentPage = lastPhotos.page + 1
        return curatedPhotosRepository.execute(
            page = currentPage,
            perPage = perPage,
        ).mapSuccess { photos ->
            if (photos.photos.isNotEmpty()) {
                val mergedPhotos = lastPhotos.copy(
                    page = currentPage,
                    photos = lastPhotos.photos + photos.photos,
                )
                writeLastSucceedPhotosRepository.setLastPhotos(mergedPhotos)
                mergedPhotos
            } else {
                lastPhotos
            }
        }
    }
}