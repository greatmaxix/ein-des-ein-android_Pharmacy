package com.pharmacy.myapp.product

import com.pharmacy.myapp.product.repository.ProductRemoteDataSource
import com.pharmacy.myapp.product.repository.ProductRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productCardModule = module {

    single { ProductRemoteDataSource(get()) }
    single { ProductRepository(get()) }

    viewModel { ProductViewModel(get()) }

    fragment { ProductFragment(get()) }
}