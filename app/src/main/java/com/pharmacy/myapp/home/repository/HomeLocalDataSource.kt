package com.pharmacy.myapp.home.repository

import com.pharmacy.myapp.categories.model.CategoryDAO
import com.pharmacy.myapp.model.category.Category

class HomeLocalDataSource(private val dao: CategoryDAO) {

    suspend fun getCategories() = dao.categories()

    suspend fun saveCategories(categories: List<Category>) = dao.insert(categories)

    suspend fun isCategoriesPresent() = dao.categoriesSize() > 0

}