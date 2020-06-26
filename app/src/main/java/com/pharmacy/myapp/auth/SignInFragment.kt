package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.ui.text.addCountryCodePrefix
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_in.*
import timber.log.Timber

class SignInFragment : BaseMVVMFragment(R.layout.fragment_sign_in) {

    private val viewModel: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tilPhoneSignIn.setPhoneRule()
        etPhoneSignIn.onDoneImeAction { mbNextSignIn.performClick() }
        mbNextSignIn.onClick {
            if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
                Timber.d("+"+etPhoneSignIn.text.toString().substring(1).replaceFirst(Regex("(\\d)(\\d{3})(\\d{3})(\\d{2})(\\d+)"), "$1 ($3) $3-$4-$5"))
                viewModel.signIn(etPhoneSignIn.text.toString())
            }
        }
        mbCreateAccount.onClick { viewModel.directionLiveData.postValue(actionFromSignInToSignUp()) }
        etPhoneSignIn.addCountryCodePrefix()
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.directionLiveData.observeExt(navController::navigate)
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }

}