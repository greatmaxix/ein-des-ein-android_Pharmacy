package com.pulse.components.auth.sign

import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.pulse.BuildConfig.DEBUG
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentDirections.Companion.actionFromSignInToSignUp
import com.pulse.core.extensions.*
import com.pulse.splash.SplashFragmentDirections.Companion.globalToHome
import com.pulse.ui.text.isPhoneNumberValid
import com.pulse.ui.text.setHintSpan
import com.pulse.ui.text.setPhoneRule
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.focusChanges

class SignInFragment : SignBaseFragment(R.layout.fragment_sign_in) {

    private val hint by lazy { (if (DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint).toString }

    private val args by navArgs<SignInFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)

        vm.popBackId = args.popBackId
        vm.nextDestinationId = args.nextDestinationId

        tilPhoneSignIn.setPhoneRule()

        etPhoneSignIn.onDoneImeAction { loginOrError() }

        nvNext.onClick { loginOrError() }

        tilPhoneSignIn.editText
            ?.focusChanges()
            ?.onEach(::notifyHint)
            ?.launchIn(viewLifecycleOwner.lifecycleScope)

        vFooter
            .setOnSkipClickListener { navController.navigate(globalToHome()) }
            .setOnActionClickListener { navController.navigate(actionFromSignInToSignUp()) }
    }

    private fun loginOrError() {
        if (tilPhoneSignIn.isPhoneNumberValid(getString(R.string.phoneErrorAuth))) {
            hideKeyboard()
            vm.signIn("${if (DEBUG) +3 else +7}${etPhoneSignIn.text()}")
        }
    }

    private fun notifyHint(focused: Boolean) {
        tilPhoneSignIn.prefixText = if (focused) if (DEBUG) "+3" else "+7" else ""
        if (focused) etPhoneSignIn.hint = null else etPhoneSignIn.setHintSpan(hint, hint.length - 1, hint.length)
    }

    override fun onBindLiveData() {
        observeResult(vm.signInLiveData) {
            onEmmit = { contentOrNull?.let(navController::navigate) }
        }
    }
}