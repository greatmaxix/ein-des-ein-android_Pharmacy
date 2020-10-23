package com.pulse.splash

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import com.pulse.splash.SplashFragmentDirections.Companion.fromSplashToHome
import com.pulse.splash.SplashFragmentDirections.Companion.fromSplashToOnBoarding
import com.pulse.splash.repository.SplashRepository
import org.koin.core.component.KoinApiExtension

class SplashViewModel(private val repository: SplashRepository, private val workManager: WorkManager) : BaseViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    @KoinApiExtension
    fun updateCustomer() {
        launchIO {
            workManager.enqueue(
                OneTimeWorkRequestBuilder<SplashWorker>()
                    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build()
            )
            _directionLiveData.postValue(repository.isNeedOnBoarding.toNavDirection)
        }
    }

    private val Boolean.toNavDirection
        get() = if (this) toHome else toOnBoarding

    companion object {
        private val toHome = fromSplashToHome()
        private val toOnBoarding = fromSplashToOnBoarding()
    }
}