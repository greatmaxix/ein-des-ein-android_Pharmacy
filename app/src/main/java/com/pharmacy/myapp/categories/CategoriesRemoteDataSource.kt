package com.pharmacy.myapp.categories

import com.pharmacy.myapp.data.remote.RestManager

class CategoriesRemoteDataSource(private val rm: RestManager) {

    suspend fun getCategories() = rm.getCategories()

}