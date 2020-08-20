package com.pharmacy.myapp.quickAccess.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao
import com.pharmacy.myapp.quickAccess.model.QuickAccess

@Dao
interface QuickAccessDAO : BaseDao<QuickAccess> {
    @WorkerThread
    @Query("SELECT * FROM quick_access")
    fun userQuickAccessLiveData(): LiveData<QuickAccess?>

    @WorkerThread
    @Query("SELECT * FROM quick_access")
    suspend fun userQuickAccess(): QuickAccess?

    @WorkerThread
    @Query("DELETE FROM quick_access")
    suspend fun deleteAll()
}