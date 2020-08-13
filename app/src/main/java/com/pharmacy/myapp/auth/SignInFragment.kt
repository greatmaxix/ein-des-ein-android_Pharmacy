package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignToHome
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : AuthBaseFragment(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authResultDestination = SignInFragmentArgs.fromBundle(requireArguments()).authResultDestination

        tilPhoneSignIn.setPhoneRule()
        etPhoneSignIn.onDoneImeAction { llButtonContainer.performClick() }
        mbCreateAccount.onClick { viewModel.directionLiveData.postValue(actionFromSignInToSignUp()) }
//        etPhoneSignIn.addCountryCodePrefix()
        llButtonContainer.onClick {
            if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
                viewModel.signIn(etPhoneSignIn.text.toString())
            }
        }
        tvSkipAuth.setDebounceOnClickListener {
            navController.navigate(actionFromSignToHome())
        }
    }
}