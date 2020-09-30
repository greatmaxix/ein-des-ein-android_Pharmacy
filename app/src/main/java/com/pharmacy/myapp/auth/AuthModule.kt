package com.pharmacy.myapp.auth

import com.pharmacy.myapp.auth.repository.AuthRemoteDataSource
import com.pharmacy.myapp.auth.repository.AuthRepository
import com.pharmacy.myapp.data.local.DBManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    single { AuthRepository(get(), get(), get<DBManager>().customerDAO) }
    single { AuthRemoteDataSource(get()) }


    viewModel { AuthViewModel(androidApplication(), get()) }

    fragment { SignInFragment() }
    fragment { SignUpFragment() }
    fragment { CodeFragment() }

}