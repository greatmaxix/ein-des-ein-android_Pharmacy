package com.pharmacy.myapp.auth

import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

val authModule = module {
//    factory { AuthRepository(get(), get(), get()) }

//    viewModel { AuthViewModel(get()) }

    fragment { SignInFragment() }
    fragment { SignUpFragment() }

}