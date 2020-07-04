package com.pharmacy.myapp.profile

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.formatPhone
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment(private val viewModel : ProfileViewModel) : BaseMVVMFragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.userDataLiveData.observeExt {
            mtvNameProfile.text = it.third
            mtvPhoneProfile.text = "+${it.second}".formatPhone()
        }
    }

}