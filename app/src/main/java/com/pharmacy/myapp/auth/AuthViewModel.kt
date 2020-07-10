package com.pharmacy.myapp.auth

import androidx.lifecycle.MutableLiveData
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
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }/*todo так легально?*/
    val userPhoneLiveData by lazy { MutableLiveData<String>() }
    private var userPhone: String = ""

    fun signUp(name: String, phone: String, email: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.signUp(name, phone.substring(1), email)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                if (response.value.code() == HTTP_CREATED) {
                    setUserPhone(phone)
                    directionLiveData.postValue(actionFromSignUpToCode())
                } else {
                    errorLiveData.postValue(response.value.message())
                }
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    fun signIn(phone: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.auth(phone.substring(1))
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                if (response.value.code() == HTTP_OK) {
                    setUserPhone(phone)
                    directionLiveData.postValue(actionFromSignInToCode())
                } else {
                    errorLiveData.postValue(response.value.message())
                }
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    fun login(code: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.login(userPhone.substring(1), code)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> directionLiveData.postValue(actionFromCodeToProfile())
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    private fun setUserPhone(phone: String) {
        var newPhone = phone
        userPhone = phone
        if (BuildConfig.DEBUG) if (!phone.contains("+")) newPhone = "+${phone.substring(1)}"
        userPhoneLiveData.postValue(newPhone.formatPhone())
    }

    fun resendCode()  = launchIO {
        progressLiveData.postValue(true)
        val response = repository.auth(userPhone)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                if (response.value.code() == HTTP_OK) {
                    // todo snackbar
                } else {
                    errorLiveData.postValue(response.value.message())
                }
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

}