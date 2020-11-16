package com.pulse.components.recipes.repository

import com.pulse.data.remote.api.RestApi

class RecipesRemoteDataSource(private val rm: RestApi) {

    suspend fun getRecipes(page: Int? = 1, pageSize: Int? = 40) = rm.getRecipes(page, pageSize)
}