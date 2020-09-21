package com.pharmacy.myapp.auth

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToCode
import com.pharmacy.myapp.auth.SignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pharmacy.myapp.auth.repository.AuthRepository
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.core.extensions.saveDrawableToFile
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo

class AuthViewModel(private var context: Context?, private val repository: AuthRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionPopBackLiveData by lazy { SingleLiveEvent<Int>() }
    val customerPhoneLiveData by lazy { MutableLiveData<String>() }
    private var customerPhone: String = ""

    var popBackId: Int = -1

    override fun onCleared() {
        super.onCleared()
        context = null
    }

    fun signUp(name: String, phone: String, email: String) {
        progressLiveData.value = true
        launchIO {
            when (val response = repository.signUp(name, phone, email)) {
                is Success -> {
                    setUserPhone(phone)
                    saveCustomerData(response.value.customer)
                    directionLiveData.postValue(actionFromSignUpToCode())
                }
                is Error -> errorLiveData.postValue(response.errorMessage)
            }
            progressLiveData.postValue(false)
        }
    }

    fun signIn(phone: String) {
        progressLiveData.value = true
        launchIO {
            val response = repository.auth(phone)
            progressLiveData.postValue(false)
            when (response) {
                is Success -> {
                    setUserPhone(phone)
                    directionLiveData.postValue(actionFromSignInToCode())
                }
                is Error -> errorLiveData.postValue(response.errorMessage)
            }
        }
    }

    fun login(code: String) {
        progressLiveData.value = true
        launchIO {
            when (val response = repository.login(customerPhone, code)) {
                is Success -> {
                    repository.saveToken(response.value.token, response.value.refreshToken)
                    saveCustomerData(response.value.customer)
                    progressLiveData.postValue(false)
                }
                is Error -> {
                    progressLiveData.postValue(false)
                    errorLiveData.postValue(response.errorMessage)
                }
            }
        }
    }

    private suspend fun saveCustomerData(customerInfo: CustomerInfo) {
        repository.saveCustomerInfo(customerInfo)?.let { context?.saveDrawableToFile(it) { homeOrPopBack() } } ?: homeOrPopBack()
    }

    private fun homeOrPopBack() {
        if (popBackId == -1) directionLiveData.postValue(globalToHome()) else directionPopBackLiveData.postValue(popBackId)
    }

    private fun setUserPhone(phone: String) {
        var newPhone = phone
        customerPhone = phone
        if (!phone.contains("+")) newPhone = "+${phone}"
        customerPhoneLiveData.postValue(newPhone.formatPhone())
    }

    fun resendCode() {
        progressLiveData.value = true
        launchIO {
            val response = repository.auth(customerPhone)
            progressLiveData.postValue(false)
            when (response) {
                is Success -> {/*todo snackbar*/ }
                is Error -> errorLiveData.postValue(response.errorMessage)
            }
        }
    }
}