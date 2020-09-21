package com.pharmacy.myapp.categories

import com.pharmacy.myapp.categories.search.CategoriesSearchFragment
import com.pharmacy.myapp.categories.search.CategoriesSearchViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesModule = module {

    fragment { CategoriesFragment(get()) }
    fragment { CategoriesSearchFragment(get()) }

    viewModel { CategoriesViewModel(get()) }
    viewModel { CategoriesSearchViewModel() }

    single { CategoriesRepository(get()) }
    single { CategoriesRemoteDataSource(get()) }
}