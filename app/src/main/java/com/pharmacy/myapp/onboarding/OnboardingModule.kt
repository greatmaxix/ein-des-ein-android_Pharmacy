package com.pharmacy.myapp.onboarding

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    fragment { OnboardingFragment() }
    fragment { OnboardingRegionFragment() }
    fragment { OnboardingAuthFragment() }

    viewModel { OnboardingViewModel(get()) }

    factory { OnboardingRepository(get()) }
}