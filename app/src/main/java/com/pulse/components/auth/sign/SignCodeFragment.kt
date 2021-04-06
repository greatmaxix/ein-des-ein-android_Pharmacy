package com.pulse.components.auth.sign

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.auth.model.AuthResult
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentCodeBinding
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges

class SignCodeFragment : SignBaseFragment(R.layout.fragment_code) {

    private val binding by viewBinding(FragmentCodeBinding::bind)

    override fun initUI() = with(binding) {
        viewCode.textChanges()
            .filter { viewCode.isCodeLength }
            .onEach { checkSmsCode() }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewCode.editorActionEvents()
            .filter { viewCode.isCodeLength && it.actionId == EditorInfo.IME_ACTION_DONE }
            .onEach { checkSmsCode() }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        mibBack.setDebounceOnClickListener { navigationBack() }

        doWithDelay(1000) {
            debug { viewCode.setText("11111") }
        }

        mbSendCodeAgain.underlineSpan()
        mbSendCodeAgain.setDebounceOnClickListener { viewModel.signInAgain() }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.customerPhoneState) { binding.mtvPhone.text = it }
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.signInEvent.events) { uiHelper.showDialog(getString(R.string.smsResendAgain)) }
        observe(viewModel.codeEvent.events, ::navigate)
    }

    private fun checkSmsCode() {
        hideKeyboard()
        if (binding.viewCode.isCodeLength) {
            viewModel.checkCode(binding.viewCode.text())
        } else {
            uiHelper.showDialog(getString(R.string.enterTheCode))
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