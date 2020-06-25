package com.pharmacy.myapp.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.pharmacy.myapp.auth.SignUpFragmentDirections.Companion.actionFromSignUpToPin
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection.HTTP_CREATED

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {

    val errorLiveData by lazy { MutableLiveData<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val directionLiveData = MutableLiveData<NavDirections>()/*todo так легально?*/

    fun signUp(name: String, phone: String, email: String) = viewModelScope.launch(Dispatchers.IO) {
        progressLiveData.postValue(true)
        val response = repository.signUp(name, phone.substring(1), email)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                if (response.value.code() == HTTP_CREATED) {
                    directionLiveData.postValue(actionFromSignUpToPin())
                } else {
                    errorLiveData.postValue(response.value.message())
                }
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }


}