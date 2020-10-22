package com.pulse.main

import com.pulse.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single { MainRepository(get(), get(), get<DBManager>().customerDAO) }

    viewModel { MainViewModel(get()) }
}