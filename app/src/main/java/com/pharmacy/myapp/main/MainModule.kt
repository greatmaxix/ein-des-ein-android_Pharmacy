package com.pharmacy.myapp.main

import com.pharmacy.myapp.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single { MainRepository(get(), get(), get<DBManager>().customerDAO) }

    viewModel { MainViewModel(get()) }
}