package com.pharmacy.myapp.splash

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.fromSplashToAuth
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.fromSplashToHome
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class SplashViewModel(private val spManager: SPManager, private val workManager: WorkManager) : BaseViewModel(), KoinComponent {

    val authenticatedLiveData by lazy { SingleLiveEvent<NavDirections>() }

    fun checkAuthentication() {
        viewModelScope.launch(IO) {
            val isTokenExists = spManager.token.isNullOrEmpty()
            if (!isTokenExists) {
                workManager.enqueue(get<OneTimeWorkRequest>(named(UPDATE_CUSTOMER_INFO))).state
            }
            val direction = isTokenExists.toNavDirection
            delay(1000)
            authenticatedLiveData.postValue(direction)
        }
    }

    private val Boolean.toNavDirection
        get() = if (this) toAuth else toHome

    companion object {

        private val toAuth = fromSplashToAuth()
        private val toHome = fromSplashToHome()
        const val UPDATE_CUSTOMER_INFO = "updateCustomerInfo"
    }
}