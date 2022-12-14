package com.pulse.components.home.repository

import com.pulse.data.remote.api.RestApi

class HomeRemoteDataSource(private val restApi: RestApi) {

    suspend fun getCategories() = restApi.categories()
        .dataOrThrow()
        .items

    suspend fun getActiveChats() = restApi.chatList()
        .dataOrThrow()
        .items

    suspend fun getChat(chatId: Int) = restApi.getChat(chatId)
        .dataOrThrow()
        .item
}