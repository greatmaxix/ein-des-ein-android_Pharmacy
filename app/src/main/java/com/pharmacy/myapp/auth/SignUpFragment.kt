package com.pharmacy.myapp.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome
import com.pharmacy.myapp.ui.text.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : AuthBaseFragment(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etEmailSignUp.onDoneImeAction { llButtonContainerSignUp.performClick() }
        tilPhoneSignUp.setPhoneRule()
        debug { tilPhoneSignUp.prefixText = "+3" }
        val hint = if (BuildConfig.DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint
        etPhoneSignUp.setAsteriskHint(hint.toString, 18, 19)
        etNameSignUp.setAsteriskHint(R.string.yourNameAuth.toString, 8, 9, false)
        llButtonContainerSignUp.onClick {
            val isNameValid = tilNameSignUp.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneSignUp.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmailSignUp.text().isNotEmpty()) tilEmailSignUp.checkEmail(getString(R.string.emailErrorAuth)) else true
            if (isNameValid && isPhoneValid && isEmailValid) {
                viewModel.signUp(tilNameSignUp.text(), tilPhoneSignUp.phoneCodePrefix + tilPhoneSignUp.text(), tilEmailSignUp.text())
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
            setSpan(ForegroundColorSpan(R.color.darkBlue.toColor), 0, 76, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
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
        mbGoToSignIn.onClick { navigationBack() }

        tvSkipAuth.setDebounceOnClickListener {
            showAlertRes(getString(R.string.messageEndAuthDialog)) {
                cancelable = false
                title = R.string.titleEndAuthDialog
                positive = R.string.buttonEndAuth
                positiveAction = { navController.navigate(globalToHome()) }
                negative = R.string.common_closeButton
            }
        }
    }
}