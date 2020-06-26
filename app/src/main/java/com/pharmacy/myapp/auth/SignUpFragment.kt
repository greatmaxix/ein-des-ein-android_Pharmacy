package com.pharmacy.myapp.auth

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.core.extensions.text
import com.pharmacy.myapp.ui.text.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseMVVMFragment(R.layout.fragment_sign_up) {

    private val viewModel: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

    private var phoneWatcher: TextWatcher? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etEmailSignUp.onDoneImeAction { mbNextSignUp.performClick() }
        tilPhoneSignUp.setPhoneRule()
        etPhoneSignUp.addCountryCodePrefix()
        mbNextSignUp.onClick {
            val isNameValid = tilNameSignUp.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneSignUp.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = tilEmailSignUp.checkEmail(getString(R.string.emailErrorAuth))
            if (isNameValid && isPhoneValid && isEmailValid) {
                viewModel.signUp(tilNameSignUp.text(), tilPhoneSignUp.text(), tilEmailSignUp.text())
            }
        }
        val clearError: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit =
            { _, _, _, _ ->
                tilNameSignUp.error = null
                tilPhoneSignUp.error = null
                tilEmailSignUp.error = null
            }
        listOf(tilNameSignUp, tilPhoneSignUp, tilEmailSignUp).onEach {
            it.editText?.doOnTextChanged(clearError)
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.directionLiveData.observeExt(navController::navigate)
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }

}