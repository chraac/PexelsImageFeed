package com.example.imagefeed.di.modules

import androidx.lifecycle.ViewModel
import com.example.imagefeed.di.utilities.ViewModelKey
import com.example.imagefeed.ui.detail.presentation.ImageDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImageDetailModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImageDetailViewModel::class)
    abstract fun bindImageDetailViewModel(viewModel: ImageDetailViewModel): ViewModel
}