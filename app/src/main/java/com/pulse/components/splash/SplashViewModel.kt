package com.pulse.components.splash

import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pulse.components.splash.SplashFragmentDirections.Companion.fromSplashToHome
import com.pulse.components.splash.SplashFragmentDirections.Companion.fromSplashToOnBoarding
import com.pulse.components.splash.repository.SplashRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashViewModel(private val repository: SplashRepository, private val workManager: WorkManager) : BaseViewModel() {

    val onboardingState = StateEventFlow(repository.isNeedOnboarding)

    fun updateCustomer() {
        workManager.enqueue(
            OneTimeWorkRequestBuilder<SplashWorker>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
        )
    }

    fun regionsOrMain() = navEvent.postEvent(repository.isNeedRegionSelection.toNavDirection)

    fun notifyOnboarding() = viewModelScope.execute {
        repository.setIsNeedOnboarding(false)
        regionsOrMain()
    }

    private val Boolean.toNavDirection
        get() = if (this) toHome else toOnboarding

    companion object {

        private val toHome = fromSplashToHome()
        private val toOnboarding = fromSplashToOnBoarding()
    }
}