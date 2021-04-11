package com.pulse.components.cart

import com.pulse.components.cart.repository.CartRemoteDataSource
import com.pulse.components.cart.repository.CartRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {

    single { CartRemoteDataSource(get()) }
    single { CartRepository(get()) }
    single { CartUseCase(get(), get()) }

    viewModel { CartViewModel(get()) }
}