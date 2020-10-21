package com.pharmacy.myapp.mercureService.repository

import androidx.room.Transaction
import com.pharmacy.myapp.chat.model.message.MessageDAO
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.chat.model.remoteKeys.RemoteKeys.Companion.createRemoteKey
import com.pharmacy.myapp.chat.model.remoteKeys.RemoteKeysDAO
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.user.model.customer.CustomerDAO
import java.time.LocalDateTime

class MercureLocalDataSource(
    private val sp: SPManager,
    private val customerDao: CustomerDAO,
    private val remoteKeysDAO: RemoteKeysDAO,
    private val messageDAO: MessageDAO,
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
}