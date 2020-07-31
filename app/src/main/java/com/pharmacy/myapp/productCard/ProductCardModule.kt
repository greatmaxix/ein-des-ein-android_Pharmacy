package com.pharmacy.myapp.productCard

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productCardModule = module {

    single { ProductCardRepository(get(), get()) }

    viewModel { ProductCardViewModel(get()) }

    fragment { ProductCardFragment(get()) }
}