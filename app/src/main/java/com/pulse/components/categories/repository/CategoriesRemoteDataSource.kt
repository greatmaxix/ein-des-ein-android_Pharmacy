package com.pulse.components.categories.repository

import com.pulse.data.remote.RestManager

class CategoriesRemoteDataSource(private val rm: RestManager) {

    suspend fun getCategories() = rm.getCategories()

}