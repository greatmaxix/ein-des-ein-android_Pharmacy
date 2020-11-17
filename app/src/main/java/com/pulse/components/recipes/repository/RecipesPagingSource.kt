package com.pulse.components.recipes.repository

import androidx.paging.PagingSource
import com.pulse.components.recipes.RecipesUseCase
import com.pulse.components.recipes.model.Recipe
import com.pulse.core.general.Event
import com.pulse.data.GeneralException

class RecipesPagingSource(private val useCase: RecipesUseCase, private val count: (Int) -> Unit, private val error: (Event<GeneralException>) -> Unit) :
    PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>) = try {
        useCase.getRecipePagerOrThrow(params.key ?: 1, params.loadSize).run {
            count(totalCount)
            LoadResult.Page(items, null, currentPageNumber + 1)
        }
    } catch (e: Throwable) {
        if (e is GeneralException) {
            error(Event(e))
        }
        LoadResult.Error(Exception(e.message))
    }
}