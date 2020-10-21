package com.pharmacy.myapp.chatType.repository

class ChatTypeRepository(
    private val lds: ChatTypeLocalDataSource,
    private val rds: ChatTypeRemoteDataSource
) {

    suspend fun createChat(type: String) = rds.createChat(type)
        .apply { lds.openedChatId = id }
}