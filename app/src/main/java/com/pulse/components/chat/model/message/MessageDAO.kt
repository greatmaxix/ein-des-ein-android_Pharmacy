package com.pulse.components.chat.model.message

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.pulse.components.chat.adapter.ChatMessageAdapter
import com.pulse.core.db.BaseDao

@Dao
interface MessageDAO : BaseDao<MessageItem> {

    @Query("SELECT * FROM MessageItem WHERE chatId = :chatId ORDER BY messageCreatedAt DESC")
    fun getMessagePagingSource(chatId: Int): PagingSource<Int, MessageItem>

    @Query("SELECT * FROM MessageItem WHERE chatId = :chatId ORDER BY messageCreatedAt DESC LIMIT 1")
    fun getLastMessageLiveData(chatId: Int): LiveData<MessageItem?>

    @Query("DELETE FROM MessageItem WHERE chatId = :chatId")
    suspend fun clearChat(chatId: Int)

    @Query("SELECT * FROM MessageItem WHERE chatId = :chatId ORDER BY messageCreatedAt ASC LIMIT 1")
    suspend fun getLastMessage(chatId: Int): MessageItem?

    @Query("SELECT * FROM MessageItem WHERE chatId = :chatId AND messageType = ${ChatMessageAdapter.TYPE_DATE_HEADER}")
    suspend fun getHeaderMessages(chatId: Int): List<MessageItem>

    @Query("SELECT * FROM MessageItem WHERE chatId = :chatId AND messageType = ${ChatMessageAdapter.TYPE_END_CHAT} LIMIT 1")
    fun getEndChatMessages(chatId: Int): MessageItem?

    @Query("SELECT * FROM MessageItem WHERE globalProductId = :globalProductId")
    suspend fun getProductMessages(globalProductId: Int): List<MessageItem>
}