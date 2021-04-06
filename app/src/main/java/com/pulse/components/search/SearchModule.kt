package com.pulse.components.search

import com.pulse.components.search.repository.SearchRemoteDataSource
import com.pulse.components.search.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@OptIn(KoinApiExtension::class)
val searchModule = module {

    single { SearchRemoteDataSource(get()) }
    single { SearchRepository(get(), get()) }

    viewModel { SearchViewModel() }
}