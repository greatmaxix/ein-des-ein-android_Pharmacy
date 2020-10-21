package com.pharmacy.myapp.chat.model.remoteKeys

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao

@Dao
interface RemoteKeysDAO : BaseDao<RemoteKeys> {

    @WorkerThread
    @Query("SELECT * FROM remotekeys WHERE messageId = :messageId")
    suspend fun getRemoteKeys(messageId: Int): RemoteKeys?

    @WorkerThread
    @Query("DELETE FROM remotekeys WHERE chatId = :chatId")
    suspend fun clearRemoteKeys(chatId: Int)
}