package com.pharmacy.myapp.profile

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.profile.ProfileFragmentDirections.Companion.actionFromProfileToEdit
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseMVVMFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivEditProfile.onClick { doNav(actionFromProfileToEdit()) }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.userDataLiveData.observeExt {
            mtvNameProfile.text = it.third
            mtvPhoneProfile.text = "+${it.second}".formatPhone()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserData()
    }

}