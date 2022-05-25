package com.mvvm.basic.di

import android.util.Log
import com.data.utility.exception.ResultCallAdapterFactory
import com.google.gson.GsonBuilder
import com.mvvm.basic.BuildConfig
import com.mvvm.basic.data.repository.remote.api.RestRepository
import com.mvvm.basic.domain.datasources.remote.api.RestDataSource
import com.mvvm.basic.domain.datasources.remote.api.RestService
import com.mvvm.basic.support.Constant.REST_API_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun restRepository(restService: RestService): RestDataSource {
        return RestRepository(restService)
    }

    @Provides
    @Singleton
    fun retrofitInstance(): Retrofit {
        val httpClient = OkHttpClient.Builder().connectTimeout(REST_API_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REST_API_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REST_API_TIMEOUT, TimeUnit.SECONDS)

        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(httpClient.apply {
            addInterceptor(HttpLoggingInterceptor {
                Log.d("Network:", it)
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(ResultCallAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideRestService(retrofit: Retrofit): RestService {
        return retrofit.create(RestService::class.java)
    }
}