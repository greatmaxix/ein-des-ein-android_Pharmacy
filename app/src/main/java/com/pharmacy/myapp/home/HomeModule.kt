package com.pharmacy.myapp.home

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    viewModel { HomeViewModel() }

    fragment { HomeFragment(get()) }

}