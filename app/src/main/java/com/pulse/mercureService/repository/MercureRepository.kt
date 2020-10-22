package com.pulse.mercureService.repository

import com.pulse.chat.model.message.MessageItem
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
}