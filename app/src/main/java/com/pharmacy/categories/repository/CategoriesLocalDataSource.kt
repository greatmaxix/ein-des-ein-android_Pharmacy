package com.pharmacy.categories.repository

import com.pharmacy.categories.model.CategoryDAO

class CategoriesLocalDataSource(private val dao: CategoryDAO) {

    suspend fun categoriesLiveData() = dao.categories()

}