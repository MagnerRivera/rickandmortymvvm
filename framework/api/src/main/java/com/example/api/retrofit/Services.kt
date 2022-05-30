package com.example.api.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyService {

    @GET("character/")
    suspend fun getPageCharacters(@Query("page") page: Int): PageCharactersResponseServer

}