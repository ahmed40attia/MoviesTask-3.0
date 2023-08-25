package com.example.movies_task30.di

import com.example.movies_task30.data.servies.ApiSeries
import com.example.movies_task30.data.servies.ApiServiceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideApiServiceImpl (api :ApiSeries) = ApiServiceImp(api)
}