package com.pulse.components.onboarding

import com.pulse.components.onboarding.repository.OnboardingRepository
import com.pulse.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OnboardingViewModel(private val repository: OnboardingRepository) : BaseViewModel() {

    fun setRegionSelectionFlag() = launchIO { repository.setRegionSelectionFlag() }
}