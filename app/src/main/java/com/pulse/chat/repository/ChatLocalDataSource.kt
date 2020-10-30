package com.pulse.chat.repository

import com.pulse.chat.model.chat.ChatItemDAO
import com.pulse.chat.model.message.MessageDAO
import com.pulse.chat.model.message.MessageItem
import com.pulse.chat.model.remoteKeys.RemoteKeys.Companion.createRemoteKey
import com.pulse.chat.model.remoteKeys.RemoteKeysDAO
import com.pulse.data.local.SPManager
import com.pulse.user.model.customer.CustomerDAO
import java.time.LocalDateTime

class ChatLocalDataSource(
    private val sp: SPManager,
    private val customerDao: CustomerDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
    private val messageDAO: MessageDAO,
    private val chatItemDAO: ChatItemDAO
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

    suspend fun removeEndChatMessage(chatId: Int) = messageDAO.getEndChatMessages(chatId)
        ?.let {
            remoteKeysDAO.deleteById(chatId, it.id)
            messageDAO.delete(it)
        }

    fun clearChat() = sp.clearChatId()

    fun getChatLiveData(chatId: Int) = chatItemDAO.getChatLiveData(chatId)
}