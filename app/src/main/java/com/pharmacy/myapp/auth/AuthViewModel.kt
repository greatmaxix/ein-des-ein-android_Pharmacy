package com.pharmacy.myapp.auth

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.auth.CodeFragmentDirections.Companion.actionFromCodeToHome
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToCode
import com.pharmacy.myapp.auth.SignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pharmacy.myapp.chat.ChatFragment.Companion.KEY_NAVIGATION_CHAT
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.model.customerInfo.CustomerInfo
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToChat
import com.pharmacy.myapp.util.AvatarUtil

class AuthViewModel(private var context: Context?, private val repository: AuthRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val customerPhoneLiveData by lazy { MutableLiveData<String>() }
    private var customerPhone: String = ""
    var authResultDestination: String? = null

    override fun onCleared() {
        super.onCleared()
        context = null
    }

    fun signUp(name: String, phone: String, email: String) {
        launchIO {
            progressLiveData.postValue(true)
            val response = repository.signUp(name, phone.substring(1), email)
            progressLiveData.postValue(false)
            when (response) {
                is Success -> {
                    setUserPhone(phone)
                    saveCustomerData(response.value.customer)
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
        val response = repository.login(customerPhone.substring(1), code)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                repository.saveToken(response.value.token, response.value.refreshToken)
                saveCustomerData(response.value.customer)
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    private suspend fun saveCustomerData(customerInfo: CustomerInfo) {
        val avatarUrl = repository.saveCustomerInfo(customerInfo)
        val action = if (authResultDestination == KEY_NAVIGATION_CHAT) {
            globalToChat()
        } else {
            actionFromCodeToHome()
        }
        if (!avatarUrl.isNullOrEmpty() && context != null) {
            AvatarUtil.saveAvatarToFile(context!!, avatarUrl) {
                directionLiveData.postValue(action)
            }
        } else {
            directionLiveData.postValue(action)
        }
    }

    private fun setUserPhone(phone: String) {
        var newPhone = phone
        customerPhone = phone
        if (BuildConfig.DEBUG) if (!phone.contains("+")) newPhone = "+${phone.substring(1)}"
        customerPhoneLiveData.postValue(newPhone.formatPhone())
    }

    fun resendCode() = launchIO {
        progressLiveData.postValue(true)
        val response = repository.auth(customerPhone)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                /*todo snackbar*/
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }
}