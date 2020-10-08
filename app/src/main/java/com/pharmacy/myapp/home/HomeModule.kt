package com.pharmacy.myapp.home

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val homeModule = module {

    viewModel { HomeViewModel() }

    fragment { HomeFragment(get()) }

}