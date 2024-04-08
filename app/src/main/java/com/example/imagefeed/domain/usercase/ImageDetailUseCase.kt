package com.example.imagefeed.domain.usercase

import com.example.imagefeed.domain.models.NoDataError
import com.example.imagefeed.domain.models.Photo
import com.example.imagefeed.domain.models.Response
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import javax.inject.Inject

class ImageDetailUseCase @Inject constructor(
    private val readLastSucceedPhotosRepository: ReadLastSucceedPhotosRepository,
) {
    fun execute(imageId: Int): Response<Photo, NoDataError> {
        val photos = readLastSucceedPhotosRepository.getLastPhotos()
        val photo = photos.photos.find { it.id == imageId }
        return if (photo != null) {
            Response.Success(photo)
        } else {
            Response.Failure(NoDataError(IllegalArgumentException("No photo with id $imageId")))
        }
    }
}