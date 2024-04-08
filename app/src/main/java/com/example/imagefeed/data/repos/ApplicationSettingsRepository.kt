package com.example.imagefeed.data.repos

import com.example.imagefeed.BuildConfig
import com.example.imagefeed.di.utilities.AppScope
import com.example.imagefeed.domain.models.ApplicationSettings
import com.example.imagefeed.domain.repos.ReadApplicationSettingsRepository
import javax.inject.Inject

@AppScope
class ApplicationSettingsRepository @Inject constructor() : ReadApplicationSettingsRepository {

    private var applicationSettings: ApplicationSettings = ApplicationSettings(
        apiKey = BuildConfig.PEXELS_API_KEY,
    )

    override fun getSetting() = applicationSettings
}