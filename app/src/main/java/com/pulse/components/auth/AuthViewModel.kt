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
import com.pulse.core.general.Event
import com.pulse.splash.SplashFragmentDirections.Companion.globalToHome

class AuthViewModel(private val repository: AuthRepository, private val workManager: WorkManager) : BaseViewModel() {

    private var phone = ""

    var popBackId: Int = -1
    var nextDestinationId: Int = -1

    /* check exist customer */
    val customerPhoneLiveData by lazy { MutableLiveData<Event<String>>() }

    val signInLiveData
        get() = customerPhoneLiveData
            .switchMap { auth ->
                requestLiveData {
                    auth?.contentOrNull?.let {
                        repository.signIn(phone)
                        actionFromSignInToCode()
                    }
                }
            }

    /* creating new customer */
    private val _signUpLiveData by lazy { MutableLiveData<Event<Auth>>() }

    val signUpLiveData
        get() = _signUpLiveData
            .switchMap { auth ->
                requestLiveData {
                    auth?.contentOrNull?.let {
                        repository.signUp(it)
                        actionFromSignUpToCode()
                    }
                }
            }

    /* sms code check */
    private val _codeLiveData by lazy { MutableLiveData<Event<String>>() }

    val codeLiveData
        get() = _codeLiveData.switchMap { auth ->
            requestLiveData {
                auth?.contentOrNull?.let {
                    repository.signCode(phone, it)?.let(::invokeAuthWorker)
                    destinationOrPopBack
                }
            }
        }

    private fun invokeAuthWorker(url: String) = workManager.enqueue(
        OneTimeWorkRequestBuilder<AuthWorker>()
            .setInputData(Data.Builder().putString(AUTH_WORKER_KEY, url).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    )

    private val destinationOrPopBack
        get() = when {
            nextDestinationId != -1 -> AuthResult.DirectionId(nextDestinationId)
            popBackId == -1 -> AuthResult.Direction(globalToHome())
            else -> AuthResult.PopBack(popBackId)
        }

    fun signUp(auth: Auth) {
        this.phone = auth.phone.formatPhone()
        _signUpLiveData.value = Event(auth)
    }

    fun signIn(phone: String) {
        this.phone = phone.formatPhone()
        customerPhoneLiveData.value = Event(this.phone)
    }

    fun checkCode(code: String) {
        _codeLiveData.value = Event(code)
    }

    fun signInAgain() = signIn(phone)

}