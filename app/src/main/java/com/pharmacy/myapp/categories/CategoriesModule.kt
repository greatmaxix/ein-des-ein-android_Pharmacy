package com.pharmacy.myapp.categories

import com.pharmacy.myapp.categories.repository.CategoriesLocalDataSource
import com.pharmacy.myapp.categories.repository.CategoriesRemoteDataSource
import com.pharmacy.myapp.categories.repository.CategoriesRepository
import com.pharmacy.myapp.categories.search.CategoriesSearchFragment
import com.pharmacy.myapp.categories.search.CategoriesSearchViewModel
import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.model.category.Category
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoriesModule = module {

    fragment { CategoriesFragment() }
    fragment { CategoriesSearchFragment(get()) }

    viewModel { (category: Category?) -> CategoriesViewModel(get(), category) }
    viewModel { CategoriesSearchViewModel() }

    single { CategoriesRepository(get(), get()) }
    single { CategoriesRemoteDataSource(get()) }
    single { CategoriesLocalDataSource(get<DBManager>().categoryDAO) }
}