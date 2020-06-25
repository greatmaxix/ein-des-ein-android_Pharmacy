package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : BaseMVVMFragment(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etPhoneSignIn.onDoneImeAction { mbNextSignIn.performClick() }
        mbNextSignIn.onClick {
            if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
                // todo make http request
            }
        }
        mbCreateAccount.onClick { navController.navigate(actionFromSignInToSignUp()) }
    }

}