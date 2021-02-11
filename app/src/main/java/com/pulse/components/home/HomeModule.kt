package com.pulse.components.home

import com.pulse.components.home.repository.HomeLocalDataSource
import com.pulse.components.home.repository.HomeRemoteDataSource
import com.pulse.components.home.repository.HomeRepository
import com.pulse.data.local.DBManager
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