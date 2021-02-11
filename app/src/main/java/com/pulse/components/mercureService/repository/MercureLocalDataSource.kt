package com.pulse.components.mercureService.repository

import androidx.room.Transaction
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chat.model.chat.ChatItemDAO
import com.pulse.components.chat.model.message.MessageDAO
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.components.chat.model.remoteKeys.RemoteKeys.Companion.createRemoteKey
import com.pulse.components.chat.model.remoteKeys.RemoteKeysDAO
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.data.local.SPManager
import java.time.LocalDateTime

class MercureLocalDataSource(
    private val sp: SPManager,
    private val customerDao: CustomerDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
    private val messageDAO: MessageDAO,
    private val chatItemDAO: ChatItemDAO
) {

    val isChatForeground: Boolean
        get() = sp.isChatForeground ?: false

    suspend fun getUserUuid() = customerDao.getCustomer()?.uuid

    suspend fun getTopicName() = customerDao.getCustomer()?.topicName

    @Transaction
    suspend fun insertMessageWithKeys(message: MessageItem) {
        messageDAO.insert(message)
        remoteKeysDAO.insert(createRemoteKey(message))
    }

    suspend fun getLastMessage(chatId: Int) = messageDAO.getLastMessage(chatId)

    suspend fun isHeaderExist(chatId: Int, createdAt: LocalDateTime) = messageDAO.getHeaderMessages(chatId)
        .find { it.createdAt.year == createdAt.year && it.createdAt.month == createdAt.month && it.createdAt.dayOfMonth == createdAt.dayOfMonth } != null

    suspend fun getChat(chatId: Int) = chatItemDAO.getChat(chatId)

    suspend fun insertChat(chat: ChatItem) = chatItemDAO.insert(chat)
}