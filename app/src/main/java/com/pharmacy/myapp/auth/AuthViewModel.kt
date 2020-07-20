package com.pharmacy.myapp.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.auth.CodeFragmentDirections.Companion.actionFromCodeToProfile
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToCode
import com.pharmacy.myapp.auth.SignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val userPhoneLiveData by lazy { MutableLiveData<String>() }
    private var userPhone: String = ""

    fun signUp(name: String, phone: String, email: String) {
        launchIO {
            progressLiveData.postValue(true)
            val response = repository.signUp(name, phone.substring(1), email)
            progressLiveData.postValue(false)
            when (response) {
                is Success -> {
                    setUserPhone(phone)
                    directionLiveData.postValue(actionFromSignUpToCode())
                }
                is Error -> errorLiveData.postValue(response.errorMessage)
            }
        }
    }

    fun signIn(phone: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.auth(phone.substring(1))
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                setUserPhone(phone)
                directionLiveData.postValue(actionFromSignInToCode())
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    fun login(code: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.login(userPhone.substring(1), code)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                repository.saveUserData(response.value.customer, response.value.token, response.value.refresh_token)
                directionLiveData.postValue(actionFromCodeToProfile())
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    private fun setUserPhone(phone: String) {
        var newPhone = phone
        userPhone = phone
        if (BuildConfig.DEBUG) if (!phone.contains("+")) newPhone = "+${phone.substring(1)}"
        userPhoneLiveData.postValue(newPhone.formatPhone())
    }

    fun resendCode() = launchIO {
        progressLiveData.postValue(true)
        val response = repository.auth(userPhone)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {/*todo snackbar*/}
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

}