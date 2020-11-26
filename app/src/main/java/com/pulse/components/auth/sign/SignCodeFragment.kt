package com.pulse.components.auth.sign

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.pulse.R
import com.pulse.components.auth.model.AuthResult
import com.pulse.core.extensions.doWithDelay
import com.pulse.core.extensions.hideKeyboard
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.underlineSpan
import kotlinx.android.synthetic.main.fragment_code.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges

class SignCodeFragment : SignBaseFragment(R.layout.fragment_code) {

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCode.textChanges()
            .filter { etCode.isCodeLength }
            .onEach { checkSmsCode() }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        etCode.editorActionEvents()
            .filter { etCode.isCodeLength && it.actionId == EditorInfo.IME_ACTION_DONE }
            .onEach { checkSmsCode() }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        btnBackCode.setDebounceOnClickListener { navigationBack() }

        doWithDelay(1000) {
            //if (BuildConfig.DEBUG) etCode.setText("11111")
        }

        btnSendCodeAgain.underlineSpan()
        btnSendCodeAgain.setDebounceOnClickListener { vm.signInAgain() }
    }

    override fun onBindLiveData() {
        observe(vm.customerPhoneLiveData) { mtvPhoneCode.text = it.peekContent }

        observeResult(vm.codeLiveData) {
            onEmmit = { this?.let(::navigate) }
        }

        observeResult(vm.signInLiveData) {
            onEmmit = { this?.let { messageCallback?.showError(R.string.smsResendAgain) } }
        }
    }

    private fun checkSmsCode() {
        hideKeyboard()
        if (etCode.isCodeLength) {
            vm.checkCode(etCode.text.toString())
        } else {
            messageCallback?.showError(R.string.enterTheCode)
        }
    }

    private fun navigate(authResult: AuthResult) {
        when (authResult) {
            is AuthResult.PopBack -> navController.popBackStack(authResult.popBackId, false)
            is AuthResult.Direction -> navController.navigate(authResult.direction)
            is AuthResult.DirectionId -> navController.navigate(authResult.directionId)
        }
    }

    private val EditText.isCodeLength
        get() = length() == 5

}