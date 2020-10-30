package com.pulse.chat.model.chat

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pulse.core.db.BaseDao

@Dao
interface ChatItemDAO : BaseDao<ChatItem> {

    @Query("DELETE FROM ChatItem")
    suspend fun clear()

    @Query("SELECT * FROM ChatItem WHERE id = :chatId  LIMIT 1")
    suspend fun getChat(chatId: Int): ChatItem?

    @Query("SELECT * FROM ChatItem WHERE id = :chatId  LIMIT 1")
    fun getChatLiveData(chatId: Int): LiveData<ChatItem?>
}