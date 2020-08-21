package com.pharmacy.myapp.quickAccess.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao
import com.pharmacy.myapp.quickAccess.model.QuickAccess.Companion.QUICK_ACCESS_TABLE_NAME

@Dao
interface QuickAccessDAO : BaseDao<QuickAccess> {

    @WorkerThread
    @Query("SELECT * FROM $QUICK_ACCESS_TABLE_NAME")
    fun userQuickAccessLiveData(): LiveData<QuickAccess?>

    @WorkerThread
    @Query("SELECT * FROM $QUICK_ACCESS_TABLE_NAME")
    suspend fun userQuickAccess(): QuickAccess?

    @WorkerThread
    @Query("DELETE FROM $QUICK_ACCESS_TABLE_NAME")
    suspend fun deleteAll()
}