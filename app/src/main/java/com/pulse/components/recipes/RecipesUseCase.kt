package com.pulse.components.recipes

import com.pulse.components.recipes.repository.RecipesRepository
import com.pulse.components.user.repository.CustomerRepository
import com.pulse.data.GeneralException

class RecipesUseCase(private val recipeRepository: RecipesRepository, private val repositoryCustomer: CustomerRepository) {

    suspend fun getRecipePagerOrThrow(page: Int? = null, pageSize: Int? = null) =
        repositoryCustomer.getCustomer()?.let { recipeRepository.recipesPaging(page, pageSize) } ?: throw GeneralException.needToLoginRecipes
}