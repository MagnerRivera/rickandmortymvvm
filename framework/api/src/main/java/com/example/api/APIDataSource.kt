package com.example.api

import com.example.PageCharacters
import com.example.api.retrofit.RickAndMortyService
import com.example.data.RemoteDataSource

class RetrofitDataSource(private val service: RickAndMortyService) :
    RemoteDataSource {

    override suspend fun getPageCharacters(page: Int): PageCharacters =
        service.getPageCharacters(page).toDomain()

}