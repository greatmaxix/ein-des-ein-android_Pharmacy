package com.pulse.components.auth.sign

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pulse.components.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentSignInBinding
import com.pulse.ui.text.fixPrefixGravity
import com.pulse.ui.text.isPhoneNumberValid
import com.pulse.ui.text.setHintSpan
import com.pulse.ui.text.setPhoneRule
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.focusChanges

class SignInFragment : SignBaseFragment(R.layout.fragment_sign_in) {

    private val args by navArgs<SignInFragmentArgs>()
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            super.onViewCreated(view, savedInstanceState)

            viewModel.popBackId = args.popBackId
            viewModel.nextDestinationId = args.nextDestinationId

            tilPhone.setPhoneRule()
            tilPhone.fixPrefixGravity()
            etPhone.onDoneImeAction { loginOrError() }
            viewNext.setDebounceOnClickListener { loginOrError() }

            tilPhone.editText
                ?.focusChanges()
                ?.onEach(::notifyHint)
                ?.launchIn(viewLifecycleOwner.lifecycleScope)

            viewFooter
                .setOnSkipClickListener { navController.navigate(globalToHome()) }
                .setOnActionClickListener { navController.navigate(actionFromSignInToSignUp()) }
        }
    }

    private fun loginOrError() {
        if (binding.tilPhone.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
            hideKeyboard()
            viewModel.signIn("${debugIfElse({ "+3" }, { "+7" })}${binding.etPhone.text()}")
        }
    }

    private fun notifyHint(focused: Boolean) {
        binding.tilPhone.prefixText = debugIfElse({ "+3" }, { "+7" })
        if (focused) binding.etPhone.hint = null else binding.etPhone.setHintSpan(phoneHint, phoneHint.length - 1, phoneHint.length)
    }

    override fun onBindLiveData() {
        observeResult(viewModel.signInLiveData) {
            onEmmit = { this?.let(navController::navigate) }
        }
    }
}