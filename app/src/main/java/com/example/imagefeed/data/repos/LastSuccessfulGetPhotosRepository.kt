package com.example.imagefeed.data.repos

import com.example.imagefeed.di.utilities.ImageFeedScope
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import com.example.imagefeed.domain.repos.WriteLastSucceedPhotosRepository
import javax.inject.Inject

@ImageFeedScope
class LastSucceedGetPhotosRepository @Inject constructor() : ReadLastSucceedPhotosRepository,
    WriteLastSucceedPhotosRepository {

    private var lastPhotos = CuratedPhotos()

    override fun setLastPhotos(photos: CuratedPhotos) {
        lastPhotos = photos
    }

    override fun clear() {
        lastPhotos = CuratedPhotos(
            page = 0,
            photos = emptyList(),
        )
    }

    override fun getLastPhotos(): CuratedPhotos =
        lastPhotos
}