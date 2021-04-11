package com.pulse.components.onboarding

import androidx.lifecycle.viewModelScope
import com.pulse.components.onboarding.repository.OnboardingRepository
import com.pulse.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OnboardingViewModel(private val repository: OnboardingRepository) : BaseViewModel() {

    fun setRegionSelectionFlag() = viewModelScope.execute { repository.setRegionSelectionFlag() }
}