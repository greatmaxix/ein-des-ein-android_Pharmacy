package com.pulse.components.recipes.repository

class RecipesRepository(private val rds: RecipesRemoteDataSource) {

    suspend fun recipesPaging(page: Int? = null, pageSize: Int? = null) = rds.getRecipes(page, pageSize).dataOrThrow()

}