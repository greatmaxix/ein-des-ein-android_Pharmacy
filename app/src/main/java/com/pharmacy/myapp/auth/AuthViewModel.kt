package com.pharmacy.myapp.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.navigation.NavDirections
import androidx.work.*
import com.pharmacy.myapp.auth.AuthSignInFragmentDirections.Companion.actionFromSignInToCode
import com.pharmacy.myapp.auth.AuthSignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pharmacy.myapp.auth.AuthWorker.Companion.AUTH_WORKER_KEY
import com.pharmacy.myapp.auth.model.SignUp
import com.pharmacy.myapp.auth.repository.AuthRepository
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.core.general.Event
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome

class AuthViewModel(private val repository: AuthRepository, private val workManager: WorkManager) : BaseViewModel() {

    private var phone = ""

    val directionLiveData by lazy { MutableLiveData<Event<NavDirections>>() }
    val directionPopBackLiveData by lazy { MutableLiveData<Event<Int>>() }

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
            repository.checkCode(phone, code)?.let(::invokeAuthWorker)
            homeOrPopBack()
        }
    }

    private fun invokeAuthWorker(url: String) = workManager.enqueue(
        OneTimeWorkRequestBuilder<AuthWorker>()
            .setInputData(Data.Builder().putString(AUTH_WORKER_KEY, url).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    )

    private fun homeOrPopBack() {
        if (popBackId == -1) directionLiveData.postValue(Event(globalToHome())) else directionPopBackLiveData.postValue(Event(popBackId))
    }

    /* creating new customer */
    private val _signUpLiveData by lazy { MutableLiveData<SignUp>() }

    val signUpLiveData = _signUpLiveData
        .switchMap { signUp ->
            requestEventLiveData {
                repository.signUp(signUp)
                actionFromSignUpToCode()
            }
        }

    /* login again */


    fun signUp(signUp: SignUp) {
        this.phone = signUp.phone.formatPhone()
        _signUpLiveData.value = signUp
    }

    fun signIn(phone: String) {
        this.phone = phone.formatPhone()
        customerPhoneLiveData.value = this.phone
    }

    fun checkCode(code: String) {
        _codeLiveData.value = code
    }

    fun signInAgain() {
        signIn(phone)
        /* progressLiveData.value = true
         launchIO {
             val response = repository.auth(customerPhone)
             progressLiveData.postValue(false)
             when (response) {
                 is Success -> {*//*todo snackbar*//* }
                is Error -> errorLiveData.postValue(response.errorMessage)
            }
        }*/
    }
}