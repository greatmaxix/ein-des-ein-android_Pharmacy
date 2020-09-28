package com.pharmacy.myapp.cart

import com.pharmacy.myapp.cart.repository.CartRemoteDataSource
import com.pharmacy.myapp.cart.repository.CartRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {

    single { CartRemoteDataSource(get()) }
    single { CartRepository(get(), get()) }

    viewModel { CartViewModel(get()) }

    fragment { CartFragment(get()) }
}