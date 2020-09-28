package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pharmacy.myapp.core.extensions.debug
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome
import com.pharmacy.myapp.ui.text.phoneCodePrefix
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.ui.text.setAsteriskHint
import com.pharmacy.myapp.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : AuthBaseFragment(R.layout.fragment_sign_in) {

    private val args by navArgs<SignInFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.popBackId = args.popBackId

        tilPhoneSignIn.setPhoneRule()
        debug { tilPhoneSignIn.prefixText = "+3" }
        val hint = if (BuildConfig.DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint
        etPhoneSignIn.setAsteriskHint(hint.toString, 18, 19)
        etPhoneSignIn.onDoneImeAction { llButtonContainer.performClick() }
        mbCreateAccount.onClick { viewModel.directionLiveData.postValue(actionFromSignInToSignUp()) }
        llButtonContainer.onClick {
            if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
                viewModel.signIn(tilPhoneSignIn.phoneCodePrefix + etPhoneSignIn.text.toString())
            }
        }
        tvSkipAuth.setDebounceOnClickListener { navController.navigate(globalToHome()) }
    }
}