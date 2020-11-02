package com.pulse.chatType.repository

import com.pulse.data.remote.api.RestApi
import com.pulse.data.remote.model.chat.CreateChatRequest

class ChatTypeRemoteDataSource(private val ra: RestApi) {

    suspend fun createChat(type: String) = ra.createChat(CreateChatRequest(type))
        .dataOrThrow()
        .item
}