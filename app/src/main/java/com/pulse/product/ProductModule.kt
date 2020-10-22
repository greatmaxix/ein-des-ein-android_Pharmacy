package com.pulse.product

import com.pulse.data.local.DBManager
import com.pulse.product.repository.ProductLocalDataSource
import com.pulse.product.repository.ProductRemoteDataSource
import com.pulse.product.repository.ProductRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val productCardModule = module {

    single { ProductRemoteDataSource(get()) }
    single { ProductLocalDataSource(get<DBManager>().recentlyViewedDAO) }
    single { ProductRepository(get(), get()) }

    viewModel { ProductViewModel() }

    fragment { ProductFragment(get()) }
}