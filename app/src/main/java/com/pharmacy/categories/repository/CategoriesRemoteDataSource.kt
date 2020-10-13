package com.pharmacy.categories.repository

import com.pharmacy.data.remote.RestManager

class CategoriesRemoteDataSource(private val rm: RestManager) {

    suspend fun getCategories() = rm.getCategories()

}