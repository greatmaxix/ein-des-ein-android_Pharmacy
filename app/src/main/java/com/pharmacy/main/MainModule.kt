package com.pharmacy.main

import com.pharmacy.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel { MainViewModel(get()) }
    factory { MainRepository(get<DBManager>().customerDAO) }
}