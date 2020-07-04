package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent

class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val userDataLiveData by lazy { SingleLiveEvent<Triple<String?, String?, String?>>() }

    fun getUserData() = userDataLiveData.postValue(repository.getUserData())
}