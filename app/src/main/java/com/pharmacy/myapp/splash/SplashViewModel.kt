package com.pharmacy.myapp.splash

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.fromSplashToAuth
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.fromSplashToProfile
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val spManager: SPManager) : BaseViewModel() {

    companion object {
        private val toAuth = fromSplashToAuth()
        private val toHome = fromSplashToProfile()
    }

    val authenticatedLiveData by lazy { SingleLiveEvent<NavDirections>() }

    fun checkAuthentication() {
        viewModelScope.launch(IO) {
            val direction = spManager.token.isNullOrEmpty().toNavDirection
            delay(1000)
            authenticatedLiveData.postValue(direction)
        }
    }

    private val Boolean.toNavDirection
        get() = if (this) toAuth else toHome
}