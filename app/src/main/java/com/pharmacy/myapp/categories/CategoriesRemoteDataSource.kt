package com.pharmacy.myapp.categories

import com.pharmacy.myapp.data.remote.rest.RestManager

class CategoriesRemoteDataSource(private val rm: RestManager) {

    suspend fun getCategories() = rm.getCategories()

}