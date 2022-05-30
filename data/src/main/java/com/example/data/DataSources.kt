package com.example.data

import com.example.PageCharacters


interface RemoteDataSource {
    suspend fun getPageCharacters(page: Int): PageCharacters
}