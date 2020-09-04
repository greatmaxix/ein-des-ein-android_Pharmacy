package com.pharmacy.myapp.categories

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesModule = module {

    fragment { CategoriesFragment(get()) }

    viewModel { CategoriesViewModel(get()) }

    single { CategoriesRepository(get()) }
    single { CategoriesRemoteDataSource(get()) }
}