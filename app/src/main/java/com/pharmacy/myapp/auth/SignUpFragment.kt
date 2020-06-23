package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.ui.text.checkEmail
import com.pharmacy.myapp.ui.text.checkLength
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.core.extensions.onDoneImeAction
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseMVVMFragment(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etEmailSignUp.onDoneImeAction { mbNextSignUp.performClick() }
        mbNextSignUp.onClick {
            val isNameValid = tilNameSignUp.checkLength(getString(R.string.nameErrorMessageAuth))
            val isPhoneValid = tilPhoneSignUp.isPhoneNumberValid(getString(R.string.phoneErrorMessageAuth))
            val isEmailValid = tilEmailSignUp.checkEmail(getString(R.string.emailErrorMessageAuth))
            if (isNameValid && isPhoneValid && isEmailValid) {
                // todo make http request
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
}