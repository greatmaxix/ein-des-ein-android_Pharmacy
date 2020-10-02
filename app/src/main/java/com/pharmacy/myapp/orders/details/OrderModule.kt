package com.pharmacy.myapp.orders.details

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val orderModule = module {

    single { OrderDetailsRepository(get()) }
    single { OrderDetailRemoteDataSource(get()) }

    viewModel { OrderDetailsViewModel(get()) }

    fragment { OrderDetailsFragment(get()) }
}