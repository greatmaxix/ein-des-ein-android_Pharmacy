package com.pulse.components.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.navigation.NavDirections
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pulse.components.splash.SplashFragmentDirections.Companion.fromSplashToHome
import com.pulse.components.splash.SplashFragmentDirections.Companion.fromSplashToOnBoarding
import com.pulse.components.splash.repository.SplashRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.Event
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashViewModel(private val repository: SplashRepository, private val workManager: WorkManager) : BaseViewModel() {

    private val _directionLiveData by lazy { MutableLiveData<Event<NavDirections>>() }
    val directionLiveData: LiveData<Event<NavDirections>> by lazy { _directionLiveData }

    val onboardingLiveData by lazy {
        liveData {
            emit(repository.isNeedOnboarding)
        }
    }

    fun updateCustomer() {
        workManager.enqueue(
            OneTimeWorkRequestBuilder<SplashWorker>()
                .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
        )
    }

    fun regionsOrMain() {
        _directionLiveData.value = Event(repository.isNeedRegionSelection.toNavDirection)
    }

    fun notifyOnboarding() {
        launchIO { repository.setIsNeedOnboarding(false) }
        regionsOrMain()
    }

    private val Boolean.toNavDirection
        get() = if (this) toHome else toOnboarding

    companion object {
        private val toHome = fromSplashToHome()
        private val toOnboarding = fromSplashToOnBoarding()
    }
}