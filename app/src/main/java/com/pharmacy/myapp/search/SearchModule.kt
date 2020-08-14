package com.pharmacy.myapp.search

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    fragment { SearchFragment(get()) }
    viewModel { SearchViewModel() }
}