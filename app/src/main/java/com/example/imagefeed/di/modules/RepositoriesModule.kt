package com.example.imagefeed.di.modules

import com.example.imagefeed.data.repos.LastSucceedGetPhotosRepository
import com.example.imagefeed.domain.repos.ReadLastSucceedPhotosRepository
import com.example.imagefeed.domain.repos.WriteLastSucceedPhotosRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoriesModule {

    @Binds
    abstract fun bindReadLastSucceedPhotosRepository(
        repository: LastSucceedGetPhotosRepository,
    ): ReadLastSucceedPhotosRepository

    @Binds
    abstract fun bindWriteLastSucceedPhotosRepository(
        repository: LastSucceedGetPhotosRepository,
    ): WriteLastSucceedPhotosRepository
}