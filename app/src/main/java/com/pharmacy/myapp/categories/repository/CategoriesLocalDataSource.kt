package com.pharmacy.myapp.categories.repository

import com.pharmacy.myapp.categories.model.CategoryDAO

class CategoriesLocalDataSource(private val dao: CategoryDAO) {

    suspend fun categoriesLiveData() = dao.categories()

}