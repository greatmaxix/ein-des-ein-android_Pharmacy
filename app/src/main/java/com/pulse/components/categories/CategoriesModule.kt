package com.pulse.components.categories

import com.pulse.components.categories.repository.CategoriesLocalDataSource
import com.pulse.components.categories.repository.CategoriesRemoteDataSource
import com.pulse.components.categories.repository.CategoriesRepository
import com.pulse.components.categories.search.CategoriesSearchViewModel
import com.pulse.data.local.DBManager
import com.pulse.model.category.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@OptIn(KoinApiExtension::class)
val categoriesModule = module {
    single { CategoriesRepository(get(), get()) }
    single { CategoriesRemoteDataSource(get()) }
    single { CategoriesLocalDataSource(get<DBManager>().categoryDAO) }

    viewModel { (category: Category?) -> CategoriesViewModel(get(), category) }
    viewModel { CategoriesSearchViewModel() }
}