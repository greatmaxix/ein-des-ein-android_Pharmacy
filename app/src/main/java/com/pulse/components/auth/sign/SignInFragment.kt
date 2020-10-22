package com.pulse.components.auth.sign

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pulse.BuildConfig
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pulse.core.extensions.debug
import com.pulse.core.extensions.onClick
import com.pulse.core.extensions.onDoneImeAction
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.ui.text.isPhoneNumberValid
import com.pulse.ui.text.phoneCodePrefix
import com.pulse.ui.text.setAsteriskHint
import com.pulse.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : SignBaseFragment(R.layout.fragment_sign_in) {

    private val args by navArgs<SignInFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.popBackId = args.popBackId
        vm.nextDestinationId = args.nextDestinationId

        tilPhoneSignIn.setPhoneRule()
        debug { tilPhoneSignIn.prefixText = "+3" }
        val hint = if (BuildConfig.DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint
        etPhoneSignIn.setAsteriskHint(hint.toString, 18, 19)
        etPhoneSignIn.onDoneImeAction { llButtonContainer.performClick() }
        mbCreateAccount.onClick { navController.navigate(actionFromSignInToSignUp()) }
        llButtonContainer.onClick {
            if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
                vm.signIn(tilPhoneSignIn.phoneCodePrefix + etPhoneSignIn.text.toString())
            }
        }
        tvSkipAuth.setDebounceOnClickListener { navController.navigate(globalToHome()) }
    }

    override fun onBindLiveData() {
        observeResult(vm.signInLiveData) {
            onEmmit = { contentOrNull?.let(navController::navigate) }
        }
    }
}