package com.pulse.components.orders

import com.pulse.components.orders.repository.OrdersRemoteDataSource
import com.pulse.components.orders.repository.OrdersRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val ordersModule = module {

    single { OrdersRepository(get()) }
    single { OrdersRemoteDataSource(get()) }

    viewModel { OrdersViewModel(get()) }

    fragment { OrdersFragment(get()) }
}