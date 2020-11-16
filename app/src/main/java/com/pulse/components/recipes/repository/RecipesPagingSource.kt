package com.pulse.components.recipes.repository

import androidx.paging.PagingSource
import com.pulse.components.recipes.model.Recipe
import timber.log.Timber

class RecipesPagingSource(private val repository: RecipesRepository, private val total: (Int) -> Unit) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        val response = repository.recipesPaging(params.key ?: 1, params.loadSize)
        total(response.totalCount)
        LoadResult.Page(response.items, null, response.currentPageNumber + 1)
    } catch (e: Exception) {
        Timber.e("Error: ${e.message}")
        LoadResult.Error(Exception(e.message))
    }
}