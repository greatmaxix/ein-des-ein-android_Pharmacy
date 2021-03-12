package com.pulse.components.home.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.components.categories.model.CategoryDAO
import com.pulse.core.extensions.clear
import com.pulse.data.local.Preferences.Chat.FIELD_OPENED_CHAT_ID
import com.pulse.model.category.Category

class HomeLocalDataSource(private val dataStore: DataStore<Preferences>, private val dao: CategoryDAO) {

    suspend fun getCategories() = dao.categories()

    suspend fun saveCategories(categories: List<Category>) = dao.insert(categories)

    suspend fun isCategoriesPresent() = dao.categoriesSize() > 0

    suspend fun clearSavedChatId() = dataStore.clear(FIELD_OPENED_CHAT_ID)
}