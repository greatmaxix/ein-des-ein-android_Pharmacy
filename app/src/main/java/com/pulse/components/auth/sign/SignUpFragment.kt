package com.pulse.components.auth.sign

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
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

    private val nameHint by lazy { getString(R.string.yourName) }
    private val binding by viewBinding(FragmentSignUpBinding::bind)
    private val fields by lazy { listOf(binding.tilName, binding.tilPhone, binding.tilEmail) }

    override fun initUI() {
        with(binding) {
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
            mtvToss.movementMethod = LinkMovementMethod.getInstance()
            mtvToss.highlightColor = Color.TRANSPARENT
            mtvToss.text = createTossSpan()
            tilPhone.editText
                ?.focusChanges()
                ?.onEach(::notifyHint)
                ?.launchIn(viewLifecycleOwner.lifecycleScope)
            viewFooter
                .setOnActionClickListener { navigationBack() }
                .setOnSkipClickListener { askConfirm() }
        }
    }

    private fun createTossSpan(): SpannableString {
        val termsOfService = getString(R.string.terms_of_service)
        val privacyPolicy = getString(R.string.privacy_policy)
        val baseString = getString(R.string.by_registering_i_agree_holder, termsOfService, privacyPolicy)
        val termsStart = baseString.indexOf(termsOfService)
        return SpannableString(baseString).apply {
            setSpan(ForegroundColorSpan(getColor(R.color.darkBlue)), 0, baseString.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            // TODO add clickable span
//            setSpan(getNavClickSpan(fromRegisterToTermsAndPrivacy(KEY_TERMS)), termsStart, termsOfService.length + termsStart, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(getColor(R.color.primaryBlue)), termsStart, termsOfService.length + termsStart, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
//            setSpan(getNavClickSpan(fromRegisterToTermsAndPrivacy(KEY_PRIVACY)), baseString.length - privacyPolicy.length, baseString.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(getColor(R.color.primaryBlue)), baseString.length - privacyPolicy.length, baseString.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    private fun getNavClickSpan(destination: NavDirections) = object : ClickableSpan() {
        override fun onClick(widget: View) {
            hideKeyboard()
            navController.navigate(destination)
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

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.signUpState.events, navController::navigate)
    }
}