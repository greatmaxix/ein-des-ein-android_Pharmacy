package com.pulse.components.auth

import com.pulse.components.auth.repository.AuthRemoteDataSource
import com.pulse.components.auth.repository.AuthRepository
import com.pulse.components.auth.sign.SignCodeFragment
import com.pulse.components.auth.sign.SignInFragment
import com.pulse.components.auth.sign.SignUpFragment
import com.pulse.data.local.DBManager
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