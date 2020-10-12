package com.pharmacy.myapp.categories.extra

import androidx.core.text.trimmedLength
import com.pharmacy.myapp.model.category.Category
import okhttp3.internal.filterList

val List<Category>.flattenCategories
    get(): List<Category> {
        fun mapCategoryToList(category: Category): List<Category> {
            val nestedCategories = mutableListOf<Category>()
            if (category.code.length == 1) nestedCategories.add(category)
            category.nodes?.onEach { nestedCategories.addAll(mapCategoryToList(it)) }?.let(nestedCategories::addAll)
            return nestedCategories
        }

        return map { mapCategoryToList(it) }.flatten().onEach {
            it.nestedCategories = it.nodes?.map { it.code }
        }
    }


val List<Category>.homeCategories
    get() = filterList { code.trimmedLength() == 1 }.subList(0, 4)