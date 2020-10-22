package com.pulse.categories

import com.pulse.categories.repository.CategoriesLocalDataSource
import com.pulse.categories.repository.CategoriesRemoteDataSource
import com.pulse.categories.repository.CategoriesRepository
import com.pulse.categories.search.CategoriesSearchFragment
import com.pulse.categories.search.CategoriesSearchViewModel
import com.pulse.data.local.DBManager
import com.pulse.model.category.Category
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

    single { CategoriesRepository(get(), get()) }
    single { CategoriesRemoteDataSource(get()) }
    single { CategoriesLocalDataSource(get<DBManager>().categoryDAO) }
}