package com.pulse.components.product

import com.pulse.components.product.repository.ProductLocalDataSource
import com.pulse.components.product.repository.ProductRemoteDataSource
import com.pulse.components.product.repository.ProductRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val productCardModule = module {

    single { ProductRemoteDataSource(get()) }
    single { ProductLocalDataSource(get<DBManager>().recentlyViewedDAO) }
    single { ProductRepository(get(), get()) }

    viewModel { ProductViewModel(get()) }
}