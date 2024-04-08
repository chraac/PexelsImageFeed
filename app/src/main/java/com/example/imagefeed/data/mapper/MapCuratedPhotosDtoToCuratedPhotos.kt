package com.example.imagefeed.data.mapper

import com.example.imagefeed.data.dtos.CuratedPhotosResponseDto
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.ImageSource
import com.example.imagefeed.domain.models.Photo
import javax.inject.Inject

class MapCuratedPhotosDtoToCuratedPhotos @Inject constructor() {
    operator fun invoke(from: CuratedPhotosResponseDto) = CuratedPhotos(
        page = from.page,
        photos = from.photos.map {
            Photo(
                id = it.id,
                width = it.width,
                height = it.height,
                photographer = it.photographer,
                photographerId = it.photographerId,
                src = ImageSource(
                    original = it.src.original,
                    large = it.src.large,
                    tiny = it.src.tiny,
                ),
                alt = it.alt,
            )
        },
    )
}