package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success

class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val userDataLiveData by lazy { SingleLiveEvent<Triple<String?, String?, String?>>() }

    fun getUserData() = userDataLiveData.postValue(repository.getUserData())

    fun updateCustomerData(name: String, email: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.updateCustomerData(name, email)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> repository.saveNewUserData(response.value)
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }
}