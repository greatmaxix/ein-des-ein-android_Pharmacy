package com.pharmacy.myapp.auth

import com.pharmacy.myapp.auth.repository.AuthRemoteDataSource
import com.pharmacy.myapp.auth.repository.AuthRepository
import com.pharmacy.myapp.auth.sign.AuthSignCodeFragment
import com.pharmacy.myapp.auth.sign.AuthSignInFragment
import com.pharmacy.myapp.auth.sign.AuthSignUpFragment
import com.pharmacy.myapp.data.local.DBManager
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    single { AuthRepository(get(), get<DBManager>().customerDAO, get()) }
    single { AuthRemoteDataSource(get()) }

    viewModel { AuthViewModel(get(), get()) }

    fragment { AuthSignInFragment() }
    fragment { AuthSignUpFragment() }
    fragment { AuthSignCodeFragment() }

}