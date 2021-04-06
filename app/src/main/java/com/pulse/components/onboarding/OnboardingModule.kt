package com.pulse.components.onboarding

import com.pulse.components.onboarding.repository.OnboardingRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val onboardingModule = module {

    single { OnboardingRepository(get()) }

    fragment { OnboardingFragment() }
    viewModel { OnboardingViewModel(get()) }
}