package com.pharmacy.myapp.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.work.*
import com.pharmacy.myapp.auth.AuthWorker.Companion.AUTH_WORKER_KEY
import com.pharmacy.myapp.auth.model.Auth
import com.pharmacy.myapp.auth.model.AuthResult
import com.pharmacy.myapp.auth.repository.AuthRepository
import com.pharmacy.myapp.auth.sign.AuthSignInFragmentDirections.Companion.actionFromSignInToCode
import com.pharmacy.myapp.auth.sign.AuthSignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome

class AuthViewModel(private val repository: AuthRepository, private val workManager: WorkManager) : BaseViewModel() {

    private var phone = ""

    var popBackId: Int = -1

    /* check exist customer */
    val customerPhoneLiveData by lazy { MutableLiveData<String>() }

    val signInLiveData = customerPhoneLiveData
        .switchMap { phone ->
            requestEventLiveData {
                repository.signIn(phone)
                actionFromSignInToCode()
            }
        }

    /* sms code check */
    private val _codeLiveData by lazy { MutableLiveData<String>() }

    val codeLiveData = _codeLiveData.switchMap { code ->
        requestLiveData {
            repository.signCode(phone, code)?.let(::invokeAuthWorker)
            homeOrPopBack
        }
    }

    private fun invokeAuthWorker(url: String) = workManager.enqueue(
        OneTimeWorkRequestBuilder<AuthWorker>()
            .setInputData(Data.Builder().putString(AUTH_WORKER_KEY, url).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    )

    private val homeOrPopBack
        get() = if (popBackId == -1) AuthResult.newInstanceDirection(globalToHome()) else AuthResult.newInstancePopBack(popBackId)

    /* creating new customer */
    private val _signUpLiveData by lazy { MutableLiveData<Auth>() }

    val signUpLiveData = _signUpLiveData
        .switchMap { signUp ->
            requestEventLiveData {
                repository.signUp(signUp)
                actionFromSignUpToCode()
            }
        }

    fun signUp(auth: Auth) {
        this.phone = auth.phone.formatPhone()
        _signUpLiveData.value = auth
    }

    fun signIn(phone: String) {
        this.phone = phone.formatPhone()
        customerPhoneLiveData.value = this.phone
    }

    fun checkCode(code: String) {
        _codeLiveData.value = code
    }

    fun signInAgain() = signIn(phone)

}