package com.pharmacy.myapp.order

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent

class OrderViewModel(private val repository: OrderRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

}