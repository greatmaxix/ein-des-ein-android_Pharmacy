package com.pulse.home.repository

import com.pulse.categories.model.CategoryDAO
import com.pulse.model.category.Category

class HomeLocalDataSource(private val dao: CategoryDAO) {

    suspend fun getCategories() = dao.categories()

    suspend fun saveCategories(categories: List<Category>) = dao.insert(categories)

    suspend fun isCategoriesPresent() = dao.categoriesSize() > 0

}