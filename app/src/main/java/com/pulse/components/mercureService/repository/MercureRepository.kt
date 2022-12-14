package com.pulse.components.mercureService.repository

import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chat.model.message.MessageItem
import java.time.LocalDateTime

class MercureRepository(private val lds: MercureLocalDataSource) {

    val isChatForeground: Boolean
        get() = lds.isChatForeground

    suspend fun getTopicName() = lds.getTopicName()

    suspend fun insertMessageWithKey(it: MessageItem) {
        it.updateMessageType(lds.getUserUuid())
        lds.insertMessageWithKeys(it)
    }

    suspend fun getLastMessage(chatId: Int) = lds.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = lds.isHeaderExist(chatId, createdAt)

    suspend fun getChat(chatId: Int) = lds.getChat(chatId)

    suspend fun insertChat(chat: ChatItem) = lds.insertChat(chat)
}