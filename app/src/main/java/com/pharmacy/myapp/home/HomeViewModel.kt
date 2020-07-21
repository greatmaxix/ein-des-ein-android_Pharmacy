package com.pharmacy.myapp.home

import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.globalToQrCodeScanner

class HomeViewModel : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }

    fun navToScanner() {
        directionLiveData.postValue(globalToQrCodeScanner())
    }
}