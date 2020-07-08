package com.pharmacy.myapp.profile.edit

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addPlusSignIfNeeded
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.core.extensions.text
import com.pharmacy.myapp.profile.ProfileViewModel
import com.pharmacy.myapp.ui.text.*
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class EditProfileFragment : BaseMVVMFragment(R.layout.fragment_profile_edit) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
        tilPhoneEditProfile.setPhoneRule()
        showBackButton(R.drawable.ic_arrow_back) { navController.popBackStack() }
        etPhoneEditProfile.addCountryCodePrefix()
        saveEditProfile.onClick {
            val isNameValid = tilNameEditProfile.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneEditProfile.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmailEditProfile.text().isNotEmpty()) tilEmailEditProfile.checkEmail(getString(R.string.emailErrorAuth)) else true
            if (isNameValid && isPhoneValid && isEmailValid) {
//                viewModel.updateUserData(tilNameEditProfile.text(), tilPhoneEditProfile.text(), tilEmailEditProfile.text()) todo
            }
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.userDataLiveData.observeExt {
            etEmailEditProfile.setText(it.first)
            etPhoneEditProfile.setText(it.second?.addPlusSignIfNeeded())
            etNameEditProfile.setText(it.third)
        }
    }

}