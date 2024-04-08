package com.example.imagefeed.domain.repos

import com.example.imagefeed.domain.models.CuratedPhotos

interface WriteLastSucceedPhotosRepository {
    fun setLastPhotos(photos: CuratedPhotos)
    fun clear()
}