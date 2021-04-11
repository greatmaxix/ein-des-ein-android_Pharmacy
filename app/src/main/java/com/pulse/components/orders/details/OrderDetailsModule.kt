package com.pulse.components.orders.details

import com.pulse.components.orders.details.repository.OrderDetailRemoteDataSource
import com.pulse.components.orders.details.repository.OrderDetailsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val orderDetailsModule = module {

    single { OrderDetailsRepository(get()) }
    single { OrderDetailRemoteDataSource(get()) }

    viewModel { OrderDetailsViewModel(get()) }
}