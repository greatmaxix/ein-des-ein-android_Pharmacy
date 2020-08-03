package com.pharmacy.myapp.order

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val orderModule = module {

    single { OrderRepository(get(), get()) }

    viewModel { OrderViewModel(get()) }

    fragment { OrderFragment(get()) }
}