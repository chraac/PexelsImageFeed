package com.example.imagefeed.di.modules

import androidx.lifecycle.ViewModel
import com.example.imagefeed.di.utilities.ViewModelKey
import com.example.imagefeed.ui.list.presentation.ImageFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImageFeedModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImageFeedViewModel::class)
    abstract fun bindImageFeedViewModel(viewModel: ImageFeedViewModel): ViewModel
}