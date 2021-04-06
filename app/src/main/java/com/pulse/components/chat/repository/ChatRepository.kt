package com.pulse.components.chat.repository

import com.pulse.components.chat.model.message.MessageItem
import com.pulse.data.remote.model.chat.SendReviewRequest
import okhttp3.MultipartBody
import java.time.LocalDateTime

class ChatRepository(
    private val rds: ChatRemoteDataSource,
    private val lds: ChatLocalDataSource
) {

    val isUserLoggedIn
        get() = lds.isUserLoggedIn

    suspend fun getCustomerUuid() = lds.getCustomerUuid()

    suspend fun fetchMessagesList(chatId: Int, pageSize: Int?, afterMessageNumber: Int?, beforeMessageNumber: Int?) =
        rds.messagesList(chatId, pageSize, afterMessageNumber, beforeMessageNumber)

    fun getMessagePagingSource(chatId: Int) = lds.getMessagePagingSource(chatId)

    suspend fun closeChat(chatId: Int) = rds.closeChat(chatId)
        .also {
            lds.clearChat()
            lds.removeEndChatMessage(chatId)
        }

    suspend fun continueChat(chatId: Int) = rds.continueChat(chatId)
        .also {
            if (!it.isClosed) lds.removeEndChatMessage(chatId)
        }

    suspend fun sendReview(chatId: Int, sendReviewRequest: SendReviewRequest) = rds.sendReview(chatId, sendReviewRequest)

    fun getLastMessageFlow(chatId: Int) = lds.getLastMessageFlow(chatId)

    suspend fun clearMessages(chatId: Int) = lds.clearMessages(chatId)

    suspend fun insertMessagesWithKeys(messages: List<MessageItem>) = lds.insertMessagesWithKeys(messages)

    suspend fun getRemoteKeys(messageId: Int) = lds.getRemoteKeys(messageId)

    suspend fun sendMessage(chatId: Int, text: String) = rds.sendMessage(chatId, text)

    suspend fun sendImageMessage(chatId: Int, fileUuid: String) = rds.sendImageMessage(chatId, fileUuid)

    suspend fun uploadImage(partBody: MultipartBody.Part) = rds.uploadImage(partBody)

    suspend fun getLastMessage(chatId: Int) = lds.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = lds.isHeaderExist(chatId, createdAt)

    fun getChatFlow(chatId: Int) = lds.getChatFlow(chatId)
}