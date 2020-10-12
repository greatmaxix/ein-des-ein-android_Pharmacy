package com.pharmacy.myapp.categories.repository

import com.pharmacy.myapp.data.remote.RestManager

class CategoriesRemoteDataSource(private val rm: RestManager) {

    suspend fun getCategories() = rm.getCategories()

}