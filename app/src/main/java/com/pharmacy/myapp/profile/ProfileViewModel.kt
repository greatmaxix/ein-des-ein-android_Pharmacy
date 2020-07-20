package com.pharmacy.myapp.profile

import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash

class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val userDataLiveData by lazy { SingleLiveEvent<Triple<String?, String?, String?>>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }

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

    fun logout() = launchIO {
        progressLiveData.postValue(true)
        val response = repository.logout()
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                repository.clearCustomerData()
                directionLiveData.postValue(actionFromProfileToSplash())
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }
}