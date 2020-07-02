package com.pharmacy.myapp.auth

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.hideKeyboard
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import kotlinx.android.synthetic.main.fragment_code.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.editorActionEvents
import reactivecircus.flowbinding.android.widget.textChanges

class CodeFragment : BaseMVVMFragment(R.layout.fragment_code) {

    private val viewModel: AuthViewModel by sharedGraphViewModel(R.id.auth_graph)

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
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.directionLiveData.observeExt(navController::navigate)
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.userPhoneLiveData) { mtvPhoneCode.text = it }
    }

    private fun checkSmsCode() {
        hideKeyboard()
            if (etCode.isCodeLength) {
                viewModel.login(etCode.text.toString())
            } else {
                viewModel.errorLiveData.postValue(R.string.enterTheCode.toString)
        }
    }

    private val EditText.isCodeLength
        get() = length() == 5

}