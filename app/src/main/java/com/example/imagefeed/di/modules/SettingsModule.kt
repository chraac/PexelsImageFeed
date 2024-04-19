package com.example.imagefeed.di.modules

import com.example.imagefeed.data.repos.ApplicationSettingsRepository
import com.example.imagefeed.domain.repos.ReadApplicationSettingsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SettingsModule {

    @Binds
    abstract fun bindReadApplicationSettingsRepository(
        repository: ApplicationSettingsRepository,
    ): ReadApplicationSettingsRepository
}