package com.pharmacy.myapp.onboarding

import com.pharmacy.myapp.onboarding.repository.OnBoardingRepository
import com.pharmacy.myapp.onboarding.tabs.OnBoardingAuthFragment
import com.pharmacy.myapp.onboarding.tabs.OnBoardingRegionFragment
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