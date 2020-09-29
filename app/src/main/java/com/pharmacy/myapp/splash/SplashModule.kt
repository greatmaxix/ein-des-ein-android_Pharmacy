package com.pharmacy.myapp.splash

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.splash.repository.SplashLocalDataSource
import com.pharmacy.myapp.splash.repository.SplashRemoteDataSource
import com.pharmacy.myapp.splash.repository.SplashRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {

    viewModel { SplashViewModel(get(), get()) }

    fragment { SplashFragment(get()) }

    single { SplashRemoteDataSource(get()) }
    single { SplashLocalDataSource(get(), get<DBManager>().customerDAO) }
    single { SplashRepository(get(), get()) }

}