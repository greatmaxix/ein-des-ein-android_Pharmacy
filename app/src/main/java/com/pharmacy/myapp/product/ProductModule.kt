package com.pharmacy.myapp.product

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.product.repository.ProductLocalDataSource
import com.pharmacy.myapp.product.repository.ProductRemoteDataSource
import com.pharmacy.myapp.product.repository.ProductRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val productCardModule = module {

    single { ProductRemoteDataSource(get()) }
    single { ProductLocalDataSource(get<DBManager>().recentlyViewedDAO) }
    single { ProductRepository(get(), get()) }

    viewModel { ProductViewModel() }

    fragment { ProductFragment(get()) }
}