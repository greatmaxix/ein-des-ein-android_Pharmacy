package com.pulse.components.splash

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val splashModule = module {

    viewModel { SplashViewModel(get(), get()) }

    fragment { SplashFragment(get()) }
}