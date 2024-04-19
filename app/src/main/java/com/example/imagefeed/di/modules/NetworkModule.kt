package com.example.imagefeed.di.modules

import com.example.imagefeed.data.services.PexelsServices
import com.example.imagefeed.domain.repos.ReadApplicationSettingsRepository
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(
        applicationSettingsRepository: ReadApplicationSettingsRepository,
    ) = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    PEXELS_API_AUTH_HEADER_KEY,
                    applicationSettingsRepository.getSetting().apiKey
                )
                .build()
            chain.proceed(request)
        }
        .build()

    @Provides
    @Named(BASE_URL_NAME)
    fun provideBaseUrl(
        applicationSettingsRepository: ReadApplicationSettingsRepository,
    ) = applicationSettingsRepository.getSetting().pexelsBaseUrl

    @Provides
    fun provideObjectMapper() = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
        configOverride(List::class.java).setterInfo = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY)
    }

    @Provides
    fun providePexelsService(
        objectMapper: ObjectMapper,
        okHttpClient: Lazy<OkHttpClient>,
        @Named(BASE_URL_NAME) baseUrl: String,
    ): PexelsServices = Retrofit.Builder()
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .baseUrl(baseUrl)
        .callFactory { okHttpClient.get().newCall(it) }
        .build()
        .create()

    companion object {
        private const val BASE_URL_NAME = "pexelsBaseUrl"
        private const val PEXELS_API_AUTH_HEADER_KEY = "Authorization"
    }
}