package com.example.imagefeed.di

import com.example.imagefeed.di.subcomponents.ImageFeedSubComponent

interface ImageFeedAppComponent {
    val imageFeedSubComponentBuilder: ImageFeedSubComponent.Builder
}