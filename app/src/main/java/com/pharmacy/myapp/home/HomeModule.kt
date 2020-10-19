package com.pharmacy.myapp.home

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.home.repository.HomeLocalDataSource
import com.pharmacy.myapp.home.repository.HomeRemoteDataSource
import com.pharmacy.myapp.home.repository.HomeRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val homeModule = module {

    single { HomeLocalDataSource(get(), get<DBManager>().categoryDAO) }
    single { HomeRemoteDataSource(get()) }
    single { HomeRepository(get(), get()) }

    viewModel { HomeViewModel(get()) }

    fragment { HomeFragment(get()) }

}