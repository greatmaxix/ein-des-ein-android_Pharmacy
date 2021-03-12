package com.pulse.components.splash

import com.pulse.components.splash.repository.SplashRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val splashModule = module {

    single { SplashRepository(get()) }

    viewModel { SplashViewModel(get(), get()) }

    fragment { SplashFragment(get()) }
}