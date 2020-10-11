package com.pharmacy.myapp.auth.sign

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.model.AuthResult
import com.pharmacy.myapp.core.extensions.hideKeyboard
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.underlineSpan
import com.pharmacy.myapp.core.general.Event
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

        btnBackCode.onClick { navigationBack() }

        if (BuildConfig.DEBUG) etCode.setText("11111")

        btnSendCodeAgain.underlineSpan()
        btnSendCodeAgain.onClick { vm.signInAgain() }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(vm.customerPhoneLiveData) { mtvPhoneCode.text = it }

        observeResult<AuthResult> {
            liveData = vm.codeLiveData
            onEmmit = { navigate(this) }
        }

        observeResult<Event<NavDirections>> {
            liveData = vm.signInLiveData
            onEmmit = { contentOrNull?.let { messageCallback?.showError(R.string.smsResendAgain) } }
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
        }
    }

    private val EditText.isCodeLength
        get() = length() == 5
}