package com.pharmacy.components.auth

import com.pharmacy.components.auth.repository.AuthRemoteDataSource
import com.pharmacy.components.auth.repository.AuthRepository
import com.pharmacy.components.auth.sign.SignCodeFragment
import com.pharmacy.components.auth.sign.SignInFragment
import com.pharmacy.components.auth.sign.SignUpFragment
import com.pharmacy.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    single { AuthRepository(get(), get<DBManager>().customerDAO, get()) }
    single { AuthRemoteDataSource(get()) }

    viewModel { AuthViewModel(get(), get()) }

    fragment { SignInFragment() }
    fragment { SignUpFragment() }
    fragment { SignCodeFragment() }

}