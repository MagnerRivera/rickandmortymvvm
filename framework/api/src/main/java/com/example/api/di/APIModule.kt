package com.example.api.di

import com.example.api.RetrofitDataSource
import com.example.api.retrofit.RickAndMortyRequest
import com.example.api.retrofit.RickAndMortyService
import com.example.data.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = "https://rickandmortyapi.com/api/"

    @Provides
    fun serviceProvider(
        @Named("baseUrl") baseUrl: String,
    ): RickAndMortyService = RickAndMortyRequest(baseUrl).getService()

    // Level data

    @Provides
    fun remoteDataSourceProvider(
        rickAndMortyService: RickAndMortyService,
    ): RemoteDataSource = RetrofitDataSource(rickAndMortyService)

}