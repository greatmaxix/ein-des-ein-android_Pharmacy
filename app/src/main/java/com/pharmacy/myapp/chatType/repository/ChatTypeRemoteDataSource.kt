package com.pharmacy.myapp.chatType.repository

import com.pharmacy.myapp.data.remote.api.RestApi
import com.pharmacy.myapp.data.remote.model.chat.CreateChatRequest

class ChatTypeRemoteDataSource(private val ra: RestApi) {

    suspend fun createChat(type: String) = ra.createChat(CreateChatRequest(type))
        .dataOrThrow()
        .item
}