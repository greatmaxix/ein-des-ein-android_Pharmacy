package com.pulse.components.chat.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.components.chat.model.chat.ChatItemDAO
import com.pulse.components.chat.model.message.MessageDAO
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.components.chat.model.remoteKeys.RemoteKeys.Companion.createRemoteKey
import com.pulse.components.chat.model.remoteKeys.RemoteKeysDAO
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Chat.FIELD_OPENED_CHAT_ID
import com.pulse.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import java.time.LocalDateTime

class ChatLocalDataSource(
    private val dataStore: DataStore<Preferences>,
    private val customerDao: CustomerDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
    private val messageDAO: MessageDAO,
    private val chatItemDAO: ChatItemDAO
) {

    val isUserLoggedIn
        get() = dataStore.getOnes(FIELD_ACCESS_TOKEN).isNullOrBlank().not()

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

    suspend fun clearChat() = dataStore.put(FIELD_OPENED_CHAT_ID, -1)

    fun getChatLiveData(chatId: Int) = chatItemDAO.getChatLiveData(chatId)
}