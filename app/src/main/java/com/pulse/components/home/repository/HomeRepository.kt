package com.pulse.components.home.repository

import com.pulse.components.categories.extra.flattenCategories
import com.pulse.components.categories.extra.homeCategories
import com.pulse.core.network.safeApiCall

class HomeRepository(private val rds: HomeRemoteDataSource, private val lds: HomeLocalDataSource) {

    suspend fun getCategories() = safeApiCall {
        if (lds.isCategoriesPresent()) {
            lds.getCategories().homeCategories
        } else {
            val categories = rds.getCategories()
            lds.saveCategories(categories.flattenCategories)
            categories.subList(0, 4)
        }
    }

    suspend fun getActiveChats() = rds.getActiveChats()

    suspend fun clearSavedChatId() = lds.clearSavedChatId()
}