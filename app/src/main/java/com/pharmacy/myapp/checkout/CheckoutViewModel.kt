package com.pharmacy.myapp.checkout

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import timber.log.Timber

class CheckoutViewModel(private val repository: CheckoutRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    val customerInfoLiveData = repository.getCustomerInfo()

    fun handlePromoCodeResult(code: String) {
        Timber.e("PROMO CODE = $code")
    }

    fun sendOrder() { // todo
        _progressLiveData.value = true
        launchIO {
            val sendOrder = repository.sendOrder("mock")
            _progressLiveData.postValue(false)
        }
    }
}