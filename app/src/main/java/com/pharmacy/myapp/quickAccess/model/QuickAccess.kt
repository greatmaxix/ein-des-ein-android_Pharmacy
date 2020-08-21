package com.pharmacy.myapp.quickAccess.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quick_access")
data class QuickAccess(@PrimaryKey val id: Int = 1, val requests: MutableList<String> = mutableListOf()) {

    fun addNewRequest(request: String): QuickAccess {
        requests.add(request)
        return this
    }

    companion object {

        val newInstance get() = QuickAccess()
    }
}