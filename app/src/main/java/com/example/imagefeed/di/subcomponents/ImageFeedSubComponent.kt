package com.example.imagefeed.di.subcomponents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imagefeed.di.modules.ImageDetailModule
import com.example.imagefeed.di.modules.ImageFeedModule
import com.example.imagefeed.di.modules.RepositoriesModule
import com.example.imagefeed.di.utilities.ImageFeedScope
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ImageFeedModule::class,
        ImageDetailModule::class,
        RepositoriesModule::class,
    ]
)
@ImageFeedScope
abstract class ImageFeedSubComponent : ViewModel() {

    abstract fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): ImageFeedSubComponent
    }
}
