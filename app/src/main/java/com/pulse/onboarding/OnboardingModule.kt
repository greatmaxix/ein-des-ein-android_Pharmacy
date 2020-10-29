package com.pulse.onboarding

import org.koin.androidx.fragment.dsl.fragment
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val onboardingModule = module {

    fragment { OnboardingFragment(get()) }

}