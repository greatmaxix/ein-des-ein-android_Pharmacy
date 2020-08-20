package com.pharmacy.myapp.search.repository

import androidx.room.Transaction
import com.pharmacy.myapp.quickAccess.model.QuickAccess
import com.pharmacy.myapp.quickAccess.model.QuickAccessDAO

class SearchLocalDataSource(private val dao: QuickAccessDAO) {

    val searchHistoryLiveData
        get() = dao.userQuickAccessLiveData()

    suspend fun searchHistory() = dao.userQuickAccess() ?: QuickAccess.newInstance

    @Transaction
    suspend fun addUserSearchHistory(text: String) = dao.insert(searchHistory().addNewRequest(text))

    suspend fun clearSearchHistory() = dao.deleteAll()
}