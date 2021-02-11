package com.pulse.components.categories.repository

import com.pulse.components.categories.model.CategoryDAO

class CategoriesLocalDataSource(private val dao: CategoryDAO) {

    suspend fun categoriesLiveData() = dao.categories()
}