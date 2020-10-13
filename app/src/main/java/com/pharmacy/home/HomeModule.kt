package com.pharmacy.home

import com.pharmacy.data.local.DBManager
import com.pharmacy.home.repository.HomeLocalDataSource
import com.pharmacy.home.repository.HomeRemoteDataSource
import com.pharmacy.home.repository.HomeRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val homeModule = module {

    single { HomeLocalDataSource(get<DBManager>().categoryDAO) }
    single { HomeRemoteDataSource(get()) }
    single { HomeRepository(get(), get()) }

    viewModel { HomeViewModel(get()) }

    fragment { HomeFragment(get()) }

}