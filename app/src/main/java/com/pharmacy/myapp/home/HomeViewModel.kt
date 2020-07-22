package com.pharmacy.myapp.home

import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent

class HomeViewModel : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
}