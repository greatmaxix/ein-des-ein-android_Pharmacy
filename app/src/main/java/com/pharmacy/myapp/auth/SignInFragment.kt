package com.pharmacy.myapp.auth

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : BaseMVVMFragment(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PhoneNumberUtils.formatNumber(etPhone.text.toString())
        etPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        etPhone.setOnEditorActionListener { v, actionId, event ->
            true
        }
    }

}