package com.pulse.components.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.work.*
import com.pulse.components.auth.AuthWorker.Companion.AUTH_WORKER_KEY
import com.pulse.components.auth.model.Auth
import com.pulse.components.auth.model.AuthResult
import com.pulse.components.auth.repository.AuthRepository
import com.pulse.components.auth.sign.SignInFragmentDirections.Companion.actionFromSignInToCode
import com.pulse.components.auth.sign.SignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.formatPhone
import com.pulse.splash.SplashFragmentDirections.Companion.globalToHome

class AuthViewModel(private val repository: AuthRepository, private val workManager: WorkManager) : BaseViewModel() {

    private var phone = ""

    var popBackId: Int = -1

    /* check exist customer */
    val customerPhoneLiveData by lazy { MutableLiveData<String>() }

    val signInLiveData by lazy {
        customerPhoneLiveData
            .switchMap { phone ->
                requestEventLiveData {
                    repository.signIn(phone)
                    actionFromSignInToCode()
                }
            }
    }

    /* sms code check */
    private val _codeLiveData by lazy { MutableLiveData<String>() }

    val codeLiveData by lazy {
        _codeLiveData.switchMap { code ->
            requestLiveData {
                repository.signCode(phone, code)?.let(::invokeAuthWorker)
                homeOrPopBack
            }
        }
    }

    private fun invokeAuthWorker(url: String) = workManager.enqueue(
        OneTimeWorkRequestBuilder<AuthWorker>()
            .setInputData(Data.Builder().putString(AUTH_WORKER_KEY, url).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    )

    private val homeOrPopBack
        get() = if (popBackId == -1) AuthResult.Direction(globalToHome()) else AuthResult.PopBack(popBackId)

    /* creating new customer */
    private val _signUpLiveData by lazy { MutableLiveData<Auth>() }

    val signUpLiveData by lazy {
        _signUpLiveData
            .switchMap { signUp ->
                requestEventLiveData {
                    repository.signUp(signUp)
                    actionFromSignUpToCode()
                }
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