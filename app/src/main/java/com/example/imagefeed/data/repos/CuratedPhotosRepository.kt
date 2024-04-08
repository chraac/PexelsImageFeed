package com.example.imagefeed.data.repos

import com.example.imagefeed.data.mapper.MapCuratedPhotosDtoToCuratedPhotos
import com.example.imagefeed.data.services.PexelsServices
import com.example.imagefeed.domain.mapper.NetworkResponseMapper
import com.example.imagefeed.domain.models.CuratedPhotos
import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import javax.inject.Inject

class CuratedPhotosRepository @Inject constructor(
    private val networkResponseMapper: NetworkResponseMapper,
    private val pexelsServices: PexelsServices,
    private val mapCuratedPhotosDtoToCuratedPhotos: MapCuratedPhotosDtoToCuratedPhotos,
) {
    suspend fun execute(
        page: Int,
        perPage: Int,
    ): Response<CuratedPhotos, NetworkError> = networkResponseMapper.asResponse {
        mapCuratedPhotosDtoToCuratedPhotos(pexelsServices.getCuratedPhotos(page, perPage))
    }
}