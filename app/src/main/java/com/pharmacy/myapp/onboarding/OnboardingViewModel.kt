package com.pharmacy.myapp.onboarding

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToAuth
import com.pharmacy.myapp.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToHome

class OnboardingViewModel(repository: OnboardingRepository) : BaseViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _skipRegionLiveData by lazy { SingleLiveEvent<String>() }
    val skipRegionLiveData by lazy { _skipRegionLiveData }

    fun skipRegion() = _skipRegionLiveData.postValue("")

    init {
        repository.setOnboardingShown()
    }

    fun goToAuth() = _directionLiveData.postValue(actionFromOnboardingToAuth())

    fun goToHome() = _directionLiveData.postValue(actionFromOnboardingToHome())
}