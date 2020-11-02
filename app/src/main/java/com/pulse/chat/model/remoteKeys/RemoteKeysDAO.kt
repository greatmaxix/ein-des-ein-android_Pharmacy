package com.pulse.chat.model.remoteKeys

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import com.pulse.core.db.BaseDao

@Dao
interface RemoteKeysDAO : BaseDao<RemoteKeys> {

    @WorkerThread
    @Query("SELECT * FROM RemoteKeys WHERE messageId = :messageId")
    suspend fun getRemoteKeys(messageId: Int): RemoteKeys?

    @WorkerThread
    @Query("DELETE FROM RemoteKeys WHERE chatId = :chatId")
    suspend fun clearRemoteKeys(chatId: Int)

    @Query("DELETE FROM RemoteKeys WHERE chatId = :chatId AND messageId = :messageId")
    suspend fun deleteById(chatId: Int, messageId: Int)
}