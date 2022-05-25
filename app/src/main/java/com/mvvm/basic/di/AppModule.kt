package com.mvvm.basic.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        NetworkModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class AppModule {

}