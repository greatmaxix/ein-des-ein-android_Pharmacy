package com.pharmacy.myapp.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToAuth
import com.pharmacy.myapp.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToHome
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingViewModel(repository: OnboardingRepository) : BaseViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _skipRegionLiveData by lazy { SingleLiveEvent<Unit>() }
    val skipRegionLiveData: LiveData<Unit> by lazy { _skipRegionLiveData }

    init {
        repository.setOnboardingShown()
    }

    fun skipRegion(needDelay: Boolean = false) {
        viewModelScope.launch {
            if (needDelay) delay(1000) // for smooth animation between screens
            _skipRegionLiveData.postValue(Unit)
        }
    }

    fun goToAuth() = _directionLiveData.postValue(actionFromOnboardingToAuth())

    fun goToHome() = _directionLiveData.postValue(actionFromOnboardingToHome())
}