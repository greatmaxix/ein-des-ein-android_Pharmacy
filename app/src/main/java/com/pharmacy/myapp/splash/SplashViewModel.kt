package com.pharmacy.myapp.splash

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.fromSplashToHome
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.fromSplashToOnBoarding
import com.pharmacy.myapp.splash.repository.SplashRepository
import kotlinx.coroutines.delay
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
            delay(1500)
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