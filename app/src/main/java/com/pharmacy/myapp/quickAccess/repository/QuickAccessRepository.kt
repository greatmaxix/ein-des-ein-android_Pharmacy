package com.pharmacy.myapp.quickAccess.repository

import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.pharmacy.myapp.quickAccess.model.QuickAccess

class QuickAccessRepository(private val lds: QuickAccessLocalDataSource) {

    val quickAccessLiveData
        get() = lds.quickAccessLiveData.switchMap { liveData { emit((it ?: QuickAccess.newInstance).requests) } }

    suspend fun addQuickAccess(text: String) = lds.addQuickAccess(text)

    suspend fun clearQuickAccess() = lds.clearQuickAccess()

}