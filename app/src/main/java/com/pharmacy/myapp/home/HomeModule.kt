package com.pharmacy.myapp.home

import com.pharmacy.myapp.home.repository.HomeRemoteDataSource
import com.pharmacy.myapp.home.repository.HomeRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    single { HomeRemoteDataSource(get()) }
    single { HomeRepository(get()) }

    viewModel { HomeViewModel(get()) }

    fragment { HomeFragment(get()) }

}