package com.pulse.components.auth.sign

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.pulse.BuildConfig.DEBUG
import com.pulse.R
import com.pulse.components.auth.model.Auth
import com.pulse.core.extensions.*
import com.pulse.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.ui.text.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.focusChanges

class SignUpFragment : SignBaseFragment(R.layout.fragment_sign_up) {

    private val phoneHint by lazy { (if (DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint).toString }
    private val nameHint by lazy { R.string.yourName.toString }

    private val fields by lazy { listOf(tilName, tilPhone, tilEmail) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBack.onClick { navigationBack() }

        etEmail.onDoneImeAction { registerOrError() }
        tilPhone.setPhoneRule()

        etName.setAsteriskHint(nameHint, nameHint.length - 1, nameHint.length, false)

        btnRegister.onClick { registerOrError() }

        fields.onEach {
            it.editText?.doOnTextChanged { _, _, _, _ ->
                tilName.error = null
                tilPhone.error = null
                tilEmail.error = null
            }
        }

        mtvToss.text = SpannableString(R.string.toss.toString).apply {
            setSpan(ForegroundColorSpan(R.color.darkBlue.toColor), 0, 76, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(R.color.primaryBlue.toColor), 26, 46, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(R.color.primaryBlue.toColor), 49, 76, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        tilPhone.editText
            ?.focusChanges()
            ?.onEach(::notifyHint)
            ?.launchIn(viewLifecycleOwner.lifecycleScope)

        vFooter
            .setOnActionClickListener { navigationBack() }
            .setOnSkipClickListener { askConfirm() }
    }

    private fun registerOrError() {
        if (isFieldsValid()) {
            hideKeyboard()
            vm.signUp(Auth(tilName.text(), (if (DEBUG) "+3" else "+7") + tilPhone.text(), tilEmail.text()))
        }
    }

    private fun isFieldsValid(): Boolean {
        val isNameValid = tilName.checkLength(getString(R.string.nameErrorAuth))
        val isPhoneValid = tilPhone.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
        val isEmailValid = if (tilEmail.text().isNotEmpty()) tilEmail.checkEmail(getString(R.string.emailErrorAuth)) else true

        return isNameValid && isPhoneValid && isEmailValid
    }

    private fun askConfirm() {
        showAlertRes(getString(R.string.messageEndAuthDialog)) {
            cancelable = false
            title = R.string.titleEndAuthDialog
            positive = R.string.buttonEndAuth
            positiveAction = { navController.navigate(globalToHome()) }
            negative = R.string.common_closeButton
        }
    }

    private fun notifyHint(focused: Boolean) {
        tilPhone.prefixText = if (focused) if (DEBUG) "+3" else "+7" else ""
        if (focused) etPhoneSignUp.hint = null else etPhoneSignUp.setHintSpan(phoneHint, phoneHint.length - 1, phoneHint.length)
    }

    override fun onBindLiveData() {
        observeResult(vm.signUpLiveData) {
            onEmmit = { contentOrNull?.let(navController::navigate) }
        }
    }
}