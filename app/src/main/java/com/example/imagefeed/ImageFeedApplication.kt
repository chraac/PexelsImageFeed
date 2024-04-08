package com.example.imagefeed

import android.app.Application
import com.example.imagefeed.di.AppComponent
import com.example.imagefeed.di.DaggerAppComponent

class ImageFeedApplication : Application() {
    val appComponent: AppComponent = DaggerAppComponent.builder().build()
}