package com.pulse.components.auth

import com.pulse.components.auth.repository.AuthRemoteDataSource
import com.pulse.components.auth.repository.AuthRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    single { AuthRepository(get(), get<DBManager>().customerDAO, get()) }
    single { AuthRemoteDataSource(get()) }

    viewModel { AuthViewModel(get(), get()) }
}