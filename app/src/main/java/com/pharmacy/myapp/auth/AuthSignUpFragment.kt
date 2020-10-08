package com.pharmacy.myapp.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavDirections
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.model.SignUp
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.core.general.Event
import com.pharmacy.myapp.splash.SplashFragmentDirections.Companion.globalToHome
import com.pharmacy.myapp.ui.text.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.focusChanges

class AuthSignUpFragment : AuthBaseFragment(R.layout.fragment_sign_up) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etEmailSignUp.onDoneImeAction { llButtonContainerSignUp.performClick() }
        tilPhoneSignUp.setPhoneRule()
        debug { tilPhoneSignUp.prefixText = "+3" }
        val hint = if (BuildConfig.DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint
        etNameSignUp.setAsteriskHint(R.string.yourNameAuth.toString, 8, 9, false)
        llButtonContainerSignUp.setDebounceOnClickListener {
            val isNameValid = tilNameSignUp.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneSignUp.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmailSignUp.text().isNotEmpty()) tilEmailSignUp.checkEmail(getString(R.string.emailErrorAuth)) else true
            if (isNameValid && isPhoneValid && isEmailValid) {
                vm.signUp(SignUp.newInstance(tilNameSignUp.text(), tilPhoneSignUp.phoneCodePrefix + tilPhoneSignUp.text(), tilEmailSignUp.text()))
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
        btnBackSignUp.setDebounceOnClickListener { navigationBack() }
        mbGoToSignIn.setDebounceOnClickListener { navigationBack() }

        tvSkipAuth.setDebounceOnClickListener {
            showAlertRes(getString(R.string.messageEndAuthDialog)) {
                cancelable = false
                title = R.string.titleEndAuthDialog
                positive = R.string.buttonEndAuth
                positiveAction = { navController.navigate(globalToHome()) }
                negative = R.string.common_closeButton
            }
        }
        tilPhoneSignUp.editText?.focusChanges()?.onEach {
            tilPhoneSignUp.prefixText = if (it) if (BuildConfig.DEBUG) "+3" else "+7" else ""
            if (it) etPhoneSignUp.hint = null else etPhoneSignUp.setHintSpan(hint.toString, 18, 19)
        }?.launchIn(CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()))
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observeRestResult<Event<NavDirections>> {
            liveData = vm.signUpLiveData
            onEmmit = { contentOrNull?.let(navController::navigate) }
        }
    }
}