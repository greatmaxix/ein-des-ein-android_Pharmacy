package com.pulse.components.auth

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.work.*
import com.pulse.components.auth.AuthWorker.Companion.AUTH_WORKER_KEY
import com.pulse.components.auth.model.Auth
import com.pulse.components.auth.model.AuthResult
import com.pulse.components.auth.repository.AuthRepository
import com.pulse.components.auth.sign.SignInFragmentDirections.Companion.actionFromSignInToCode
import com.pulse.components.auth.sign.SignUpFragmentDirections.Companion.actionFromSignUpToCode
import com.pulse.components.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.formatPhone
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow

class AuthViewModel(private val repository: AuthRepository, private val workManager: WorkManager) : BaseViewModel() {

    private var phone = ""

    var popBackId: Int = -1
    var nextDestinationId: Int = -1

    val customerPhoneState = StateEventFlow("")
    val signInEvent = SingleShotEvent<NavDirections>()
    val signUpState = SingleShotEvent<NavDirections>()
    val codeEvent = SingleShotEvent<AuthResult>()

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
        viewModelScope.execute {
            repository.signUp(auth)
            signUpState.offerEvent(actionFromSignUpToCode())
        }
    }

    fun signIn(phone: String) {
        this.phone = phone.substring(1)
        customerPhoneState.postState(this.phone)
        viewModelScope.execute {
            repository.signIn(this.phone)
            signInEvent.offerEvent(actionFromSignInToCode())
        }
    }

    fun checkCode(code: String) {
        viewModelScope.execute {
            repository.signCode(phone, code)?.let(::invokeAuthWorker)
            codeEvent.offerEvent(destinationOrPopBack)
        }
    }

    fun signInAgain() = signIn(phone)
}