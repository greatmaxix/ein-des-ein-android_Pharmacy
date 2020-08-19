package com.pharmacy.myapp.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignUpFragmentDirections.Companion.actionFromSignUpToHome
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.ui.text.checkEmail
import com.pharmacy.myapp.ui.text.checkLength
import com.pharmacy.myapp.ui.text.isPhoneNumberValid
import com.pharmacy.myapp.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : AuthBaseFragment(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etEmailSignUp.onDoneImeAction { llButtonContainerSignUp.performClick() }
        tilPhoneSignUp.setPhoneRule()
//        etPhoneSignUp.addCountryCodePrefix()
        llButtonContainerSignUp.onClick {
            val isNameValid = tilNameSignUp.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneSignUp.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmailSignUp.text().isNotEmpty()) tilEmailSignUp.checkEmail(getString(R.string.emailErrorAuth)) else true
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
        mtvToss.text = SpannableString(R.string.toss.toString).apply {
            setSpan(ForegroundColorSpan(R.color.primaryBlue.toColor), 26, 46, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(R.color.primaryBlue.toColor), 49, 76, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
/*
            setSpan(object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }

                override fun onClick(widget: View) {
                    Timber.d("onClick")
                    // todo go to proper screen
                }
            }, 26, 46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
*/
        }
        btnBackSignUp.onClick { navigationBack() }

        tvSkipAuth.setDebounceOnClickListener {
            showAlertRes(getString(R.string.messageEndAuthDialog)) {
                cancelable = false
                title = R.string.titleEndAuthDialog
                positive = R.string.buttonEndAuth
                positiveAction = { navController.navigate(actionFromSignUpToHome()) }
                negative = R.string.common_closeButton
            }
        }
    }
}