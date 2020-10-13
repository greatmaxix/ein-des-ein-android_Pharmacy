package com.pharmacy.onboarding

import com.pharmacy.onboarding.repository.OnBoardingRepository
import com.pharmacy.onboarding.tabs.OnBoardingAuthFragment
import com.pharmacy.onboarding.tabs.OnBoardingRegionFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {

    fragment { OnBoardingFragment() }
    fragment { OnBoardingRegionFragment() }
    fragment { OnBoardingAuthFragment() }

    viewModel { OnBoardingViewModel(get()) }

    factory { OnBoardingRepository(get()) }
}