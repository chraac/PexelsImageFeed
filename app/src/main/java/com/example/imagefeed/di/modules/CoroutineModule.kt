package com.example.imagefeed.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named

@Module
class CoroutineModule {

    @Provides
    @Named(IO_DISPATCHER)
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(IO_SCOPE)
    fun provideIoCoroutineScope(
        @Named(IO_DISPATCHER) dispatcher: CoroutineDispatcher
    ): CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcher)

    companion object {
        private const val IO_DISPATCHER = "ioDispatcher"
        const val IO_SCOPE = "ioScope"
    }
}