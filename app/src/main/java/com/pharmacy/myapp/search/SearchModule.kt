package com.pharmacy.myapp.search

import com.pharmacy.myapp.search.repository.SearchRemoteDataSource
import com.pharmacy.myapp.search.repository.SearchRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    single { SearchRemoteDataSource(get()) }
    single { SearchRepository(get(), get(), get()) }

    viewModel { SearchViewModel(get()) }

    fragment { SearchFragment(get()) }
}