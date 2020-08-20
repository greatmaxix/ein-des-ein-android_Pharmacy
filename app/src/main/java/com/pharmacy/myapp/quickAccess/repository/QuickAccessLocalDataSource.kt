package com.pharmacy.myapp.quickAccess.repository

import androidx.room.Transaction
import com.pharmacy.myapp.quickAccess.model.QuickAccess
import com.pharmacy.myapp.quickAccess.model.QuickAccessDAO

class QuickAccessLocalDataSource(private val dao: QuickAccessDAO) {

    val quickAccessLiveData
        get() = dao.userQuickAccessLiveData()

    @Transaction
    suspend fun addQuickAccess(text: String) = dao.insert((dao.userQuickAccess() ?: QuickAccess.newInstance).addNewRequest(text))
    
    suspend fun clearQuickAccess() = dao.deleteAll()

}