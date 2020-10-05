package com.pharmacy.myapp.orders.details

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Success

class OrderDetailsViewModel(private val repository: OrderDetailsRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _isCancelSuccessLiveData by lazy { SingleLiveEvent<Boolean>() }
    val isCancelSuccessLiveData: LiveData<Boolean> by lazy { _isCancelSuccessLiveData }

    fun cancelOrder(id: Int) {
        _progressLiveData.value = true
        launchIO {
            val response = repository.cancelOrder(id)
            _progressLiveData.postValue(false)
            _isCancelSuccessLiveData.postValue(response is Success)
        }
    }

}