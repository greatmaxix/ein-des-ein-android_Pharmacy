package com.pulse.components.analyzes.category

import com.pulse.components.analyzes.category.repository.AnalyzeCategoriesLocalDataSource
import com.pulse.components.analyzes.category.repository.AnalyzeCategoriesRemoteDataSource
import com.pulse.components.analyzes.category.repository.AnalyzeCategoriesRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val analyzeCategoriesModule = module {

    viewModel { (args: AnalyzeCategoriesFragmentArgs) -> AnalyzeCategoriesViewModel(args, get()) }
    fragment { AnalyzeCategoriesFragment() }

    single { AnalyzeCategoriesRepository(get(), get()) }
    single { AnalyzeCategoriesRemoteDataSource(get()) }
    single { AnalyzeCategoriesLocalDataSource() }
}