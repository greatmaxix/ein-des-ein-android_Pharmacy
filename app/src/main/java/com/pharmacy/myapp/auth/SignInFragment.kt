package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import com.pharmacy.myapp.core.extensions.visibleOrInvisible
import com.pharmacy.myapp.ui.text.addAfterTextWatcher
import com.pharmacy.myapp.ui.text.addCountryCodePrefix
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : AuthBaseFragment(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tilPhoneSignIn.setPhoneRule()
        etPhoneSignIn.onDoneImeAction { llButtonContainer.performClick() }
        mbCreateAccount.onClick { viewModel.directionLiveData.postValue(actionFromSignInToSignUp()) }
//        etPhoneSignIn.addCountryCodePrefix()
        llButtonContainer.onClick {
            if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
                viewModel.signIn(etPhoneSignIn.text.toString())
            }
        }
        etPhoneSignIn.addAfterTextWatcher { ibClearPhoneSignIn.visibleOrInvisible(it.isNotEmpty() && it != "+7") }
        ibClearPhoneSignIn.onClick { etPhoneSignIn.setText("") }
    }

}