package com.pharmacy.myapp.chat.repository

import com.pharmacy.myapp.chat.model.message.MessageDAO
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.chat.model.remoteKeys.RemoteKeys.Companion.createRemoteKey
import com.pharmacy.myapp.chat.model.remoteKeys.RemoteKeysDAO
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.user.model.customer.CustomerDAO
import java.time.LocalDateTime

class ChatLocalDataSource(
    private val sp: SPManager,
    private val customerDao: CustomerDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
    private val messageDAO: MessageDAO,
) {

    val isUserLoggedIn
        get() = !sp.token.isNullOrBlank()

    suspend fun getCustomerUuid() = customerDao.getCustomer()?.uuid


    fun getMessagePagingSource(chatId: Int) = messageDAO.getMessagePagingSource(chatId)

    fun getLastMessageLiveData(chatId: Int) = messageDAO.getLastMessageLiveData(chatId)

    suspend fun getRemoteKeys(messageId: Int) = remoteKeysDAO.getRemoteKeys(messageId)

    suspend fun clearMessages(chatId: Int) {
        messageDAO.clearChat(chatId)
        remoteKeysDAO.clearRemoteKeys(chatId)
    }

    suspend fun insertMessagesWithKeys(messages: List<MessageItem>) {
        val keys = messages.map { messageItem -> createRemoteKey(messageItem) }
        messageDAO.insert(messages)
        remoteKeysDAO.insert(keys)
    }

    suspend fun getLastMessage(chatId: Int) = messageDAO.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = messageDAO.getHeaderMessages(chatId)
        .find { it.createdAt.year == createdAt.year && it.createdAt.month == createdAt.month && it.createdAt.dayOfMonth == createdAt.dayOfMonth } != null

    suspend fun removeEndChatMessage(chatId: Int) = messageDAO.getEndChatMessage(chatId)?.let { messageDAO.delete(it) }

    fun closeChat() = sp.clearChatId()
}