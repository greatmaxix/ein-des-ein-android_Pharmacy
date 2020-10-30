package com.pulse.home.repository

import com.pulse.categories.extra.flattenCategories
import com.pulse.categories.extra.homeCategories
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

    suspend fun getCurrentChat() = lds.openedChatId
        ?.let { rds.getChat(it) }

    fun clearSavedChatId() = lds.clearSavedChatId()
}