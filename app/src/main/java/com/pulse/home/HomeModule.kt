package com.pulse.home

import com.pulse.data.local.DBManager
import com.pulse.home.repository.HomeLocalDataSource
import com.pulse.home.repository.HomeRemoteDataSource
import com.pulse.home.repository.HomeRepository
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