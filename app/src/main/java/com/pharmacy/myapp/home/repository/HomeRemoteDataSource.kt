package com.pharmacy.myapp.home.repository

import com.pharmacy.myapp.data.remote.api.RestApi

class HomeRemoteDataSource(private val restApi: RestApi) {

    suspend fun getCategories() = restApi.categories()
        .dataOrThrow()
        .items

    suspend fun getChat(chatId: Int) = restApi.getChat(chatId)
        .dataOrThrow()
        .item
}