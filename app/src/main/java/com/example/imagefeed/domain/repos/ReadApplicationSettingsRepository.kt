package com.example.imagefeed.domain.repos

import com.example.imagefeed.domain.models.ApplicationSettings

interface ReadApplicationSettingsRepository {
    fun getSetting(): ApplicationSettings
}