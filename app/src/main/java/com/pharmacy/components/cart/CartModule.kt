package com.pharmacy.components.cart

import com.pharmacy.components.cart.repository.CartRemoteDataSource
import com.pharmacy.components.cart.repository.CartRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {

    single { CartRemoteDataSource(get()) }
    single { CartRepository(get()) }
    single { CartUseCase(get(), get()) }

    viewModel { CartViewModel(get()) }

    fragment { CartFragment(get()) }
}