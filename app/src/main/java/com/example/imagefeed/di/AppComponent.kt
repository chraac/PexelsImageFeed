package com.example.imagefeed.di

import com.example.imagefeed.di.modules.CoroutineModule
import com.example.imagefeed.di.modules.NetworkModule
import com.example.imagefeed.di.modules.SettingsModule
import com.example.imagefeed.di.modules.ViewModelFactoryModule
import com.example.imagefeed.di.utilities.AppScope
import dagger.Component

@Component(
    modules = [
        ViewModelFactoryModule::class,
        NetworkModule::class,
        CoroutineModule::class,
        SettingsModule::class,
    ]
)
@AppScope
abstract class AppComponent : ImageFeedAppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}