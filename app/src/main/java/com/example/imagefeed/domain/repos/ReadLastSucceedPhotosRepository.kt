package com.example.imagefeed.domain.repos

import com.example.imagefeed.domain.models.CuratedPhotos

interface ReadLastSucceedPhotosRepository {
    fun getLastPhotos(): CuratedPhotos
}