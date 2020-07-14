package com.pharmacy.myapp.auth

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    single { AuthRepository(get(), get()) }

    viewModel { AuthViewModel(get()) }

    fragment { SignInFragment() }
    fragment { SignUpFragment() }

}