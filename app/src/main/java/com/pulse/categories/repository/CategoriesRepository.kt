package com.pulse.categories.repository

class CategoriesRepository(private val rds: CategoriesRemoteDataSource, private val lds: CategoriesLocalDataSource) {

    suspend fun getLocalCategories() = lds.categoriesLiveData()

    suspend fun getCategories() = rds.getCategories()


}