package com.pharmacy.myapp.categories

import com.pharmacy.myapp.categories.search.CategoriesSearchFragment
import com.pharmacy.myapp.categories.search.CategoriesSearchViewModel
import com.pharmacy.myapp.model.category.Category
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val categoriesModule = module {

    fragment { CategoriesFragment() }
    fragment { CategoriesSearchFragment(get()) }

    viewModel { (category: Category?) -> CategoriesViewModel(get(), category) }
    viewModel { CategoriesSearchViewModel() }

    single { CategoriesRepository(get()) }
    single { CategoriesRemoteDataSource(get()) }
}