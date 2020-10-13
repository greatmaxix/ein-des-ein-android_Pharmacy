package com.pharmacy.home.repository

import com.pharmacy.categories.extra.flattenCategories
import com.pharmacy.categories.extra.homeCategories
import com.pharmacy.core.network.safeApiCall

class HomeRepository(private val rds: HomeRemoteDataSource, private val lds: HomeLocalDataSource) {

    suspend fun getCategories() = safeApiCall {
        if (lds.isCategoriesPresent()) {
            lds.getCategories().homeCategories
        } else {
            val categories = rds.getCategories().data.items
            lds.saveCategories(categories.flattenCategories)
            categories.subList(0, 4)
        }
    }

}