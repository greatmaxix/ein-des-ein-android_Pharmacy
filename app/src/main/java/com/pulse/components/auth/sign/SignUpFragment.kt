package com.pulse.components.auth.sign

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.model.Auth
import com.pulse.components.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentSignUpBinding
import com.pulse.ui.text.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.focusChanges

class SignUpFragment : SignBaseFragment(R.layout.fragment_sign_up) {

    private val nameHint by lazy { R.string.yourName.toString }
    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val fields by lazy { listOf(binding.tilName, binding.tilPhone, binding.tilEmail) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            super.onViewCreated(view, savedInstanceState)

            ivBack.setDebounceOnClickListener { navigationBack() }
            etEmail.onDoneImeAction { registerOrError() }
            tilPhone.setPhoneRule()
            tilPhone.fixPrefixGravity()
            etName.setAsteriskHint(nameHint, nameHint.length - 1, nameHint.length, false)
            mbRegister.setDebounceOnClickListener { registerOrError() }

            fields.onEach {
                it.editText?.doOnTextChanged { _, _, _, _ ->
                    tilName.error = null
                    tilPhone.error = null
                    tilEmail.error = null
                }
            }

            mtvToss.text = SpannableString(getString(R.string.toss)).apply {
                setSpan(ForegroundColorSpan(R.color.darkBlue.toColor), 0, 76, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(ForegroundColorSpan(R.color.primaryBlue.toColor), 26, 46, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(ForegroundColorSpan(R.color.primaryBlue.toColor), 49, 76, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }

            tilPhone.editText
                ?.focusChanges()
                ?.onEach(::notifyHint)
                ?.launchIn(viewLifecycleOwner.lifecycleScope)

            viewFooter
                .setOnActionClickListener { navigationBack() }
                .setOnSkipClickListener { askConfirm() }
        }
    }

    private fun registerOrError() {
        if (isFieldsValid()) {
            hideKeyboard()
            viewModel.signUp(Auth(binding.tilName.text(), (debugIfElse({ "3" }, { "7" })) + binding.tilPhone.text(), binding.tilEmail.text()))
        }
    }

    private fun isFieldsValid(): Boolean {
        val isNameValid = binding.tilName.checkLength(getString(R.string.nameErrorAuth))
        val isPhoneValid = binding.tilPhone.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
        val isEmailValid = if (binding.tilEmail.text().isNotEmpty()) binding.tilEmail.checkEmail(getString(R.string.emailErrorAuth)) else true

        return isNameValid && isPhoneValid && isEmailValid
    }

    private fun askConfirm() {
        showAlertRes(getString(R.string.messageEndAuthDialog)) {
            cancelable = false
            title = R.string.titleEndAuthDialog
            positive = R.string.buttonEndAuth
            positiveAction = { navController.navigate(globalToHome()) }
            negative = R.string.cancel
        }
    }

    private fun notifyHint(focused: Boolean) {
        binding.tilPhone.prefixText = if (focused) debugIfElse({ "+3" }, { "+7" }) else ""
        if (focused) binding.etPhone.hint = null else binding.etPhone.setHintSpan(phoneHint, phoneHint.length - 1, phoneHint.length)
    }

    override fun onBindLiveData() {
        observeResult(viewModel.signUpLiveData) {
            onEmmit = { this?.let(navController::navigate) }
        }
    }
}