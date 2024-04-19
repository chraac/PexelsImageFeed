package com.example.imagefeed.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.imagefeed.di.utilities.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelFactoryModule {
    @Binds
    fun viewModelFactory(impl: ViewModelFactory): ViewModelProvider.Factory
}