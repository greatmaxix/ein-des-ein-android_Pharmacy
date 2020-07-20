package com.pharmacy.myapp.splash

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object SplashModule {
    val splashModule = module {
        viewModel { SplashViewModel(get()) }
        fragment { SplashFragment(get()) }
    }
}