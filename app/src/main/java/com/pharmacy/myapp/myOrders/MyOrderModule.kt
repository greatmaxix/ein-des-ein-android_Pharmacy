package com.pharmacy.myapp.myOrders

import com.pharmacy.myapp.myOrders.repository.MyOrdersRemoteDataSource
import com.pharmacy.myapp.myOrders.repository.MyOrdersRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val myOrdersModule = module {

    single { MyOrdersRepository(get()) }
    single { MyOrdersRemoteDataSource(get()) }

    viewModel { MyOrdersViewModel() }

    fragment { MyOrdersFragment(get()) }
}