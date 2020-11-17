package com.pulse.components.recipes.repository

import com.pulse.data.remote.api.RestApi

class RecipesRemoteDataSource(private val rm: RestApi) {

    suspend fun getRecipes(page: Int?, pageSize: Int?) = rm.getRecipes(page, pageSize)
}