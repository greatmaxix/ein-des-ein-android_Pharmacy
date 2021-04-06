package com.pulse.components.recipes

import com.pulse.components.recipes.repository.RecipesRemoteDataSource
import com.pulse.components.recipes.repository.RecipesRepository
import com.pulse.components.recipes.repository.RecipesUseCase
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val recipesModule = module {
    single { RecipesUseCase(get(), get()) }
    single { RecipesRemoteDataSource(get()) }
    single { RecipesRepository(get()) }

    viewModel { RecipesViewModel(get()) }

    fragment { RecipesFragment(get()) }
}