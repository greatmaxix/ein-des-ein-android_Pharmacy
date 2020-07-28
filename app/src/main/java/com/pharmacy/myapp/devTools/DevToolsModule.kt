package com.pharmacy.myapp.devTools

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val devToolsModule = module {

    single { DevToolsRepository(get(), get()) }

    viewModel { DevToolsViewModel(get()) }

    fragment { DevToolsFragment(get()) }
}