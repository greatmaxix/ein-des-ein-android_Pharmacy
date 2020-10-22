package com.pulse.splash

import com.pulse.data.local.DBManager
import com.pulse.splash.repository.SplashLocalDataSource
import com.pulse.splash.repository.SplashRemoteDataSource
import com.pulse.splash.repository.SplashRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val splashModule = module {

    viewModel { SplashViewModel(get(), get()) }

    fragment { SplashFragment(get()) }

    single { SplashRemoteDataSource(get()) }
    single { SplashLocalDataSource(get(), get<DBManager>().customerDAO) }
    single { SplashRepository(get(), get()) }

}