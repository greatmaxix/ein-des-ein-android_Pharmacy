package com.pulse.components.search

import com.pulse.components.search.repository.SearchRemoteDataSource
import com.pulse.components.search.repository.SearchRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val searchModule = module {

    single { SearchRemoteDataSource(get()) }
    single { SearchRepository(get()) }

    viewModel { SearchViewModel() }

    fragment { SearchFragment(get()) }
}