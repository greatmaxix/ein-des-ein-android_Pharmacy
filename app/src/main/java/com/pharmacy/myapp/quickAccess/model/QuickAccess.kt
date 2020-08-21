package com.pharmacy.myapp.quickAccess.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pharmacy.myapp.quickAccess.model.QuickAccess.Companion.QUICK_ACCESS_TABLE_NAME

@Entity(tableName = QUICK_ACCESS_TABLE_NAME)
data class QuickAccess(@PrimaryKey val id: Int = 1, val requests: MutableList<String> = mutableListOf()) {

    fun addNewRequest(request: String): QuickAccess {
        requests.add(request)
        return this
    }

    companion object {

        const val QUICK_ACCESS_TABLE_NAME = "quick_access"

        val newInstance get() = QuickAccess()
    }
}