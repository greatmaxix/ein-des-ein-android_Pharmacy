package com.pharmacy.myapp.categories

class CategoriesRepository(private val rds: CategoriesRemoteDataSource) {

    suspend fun getCategories() = rds.getCategories()


}