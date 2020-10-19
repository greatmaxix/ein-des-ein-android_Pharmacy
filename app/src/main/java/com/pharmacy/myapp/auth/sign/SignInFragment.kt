package com.pharmacy.myapp.auth.sign

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.sign.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pharmacy.myapp.core.extensions.debug
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.core.general.Event
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.ui.text.phoneCodePrefix
import com.pharmacy.myapp.ui.text.setAsteriskHint
import com.pharmacy.myapp.ui.text.setPhoneRule
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
        observeResult<Event<NavDirections>> {
            liveData = vm.signInLiveData
            onEmmit = { contentOrNull?.let(navController::navigate) }
        }
    }
}