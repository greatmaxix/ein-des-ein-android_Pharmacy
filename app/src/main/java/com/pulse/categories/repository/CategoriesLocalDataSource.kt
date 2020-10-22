package com.pulse.categories.repository

import com.pulse.categories.model.CategoryDAO

class CategoriesLocalDataSource(private val dao: CategoryDAO) {

    suspend fun categoriesLiveData() = dao.categories()

}