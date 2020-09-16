package com.pharmacy.myapp.user.profile.guest

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToRegion
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.user.profile.guest.GuestProfileFragmentDirections.Companion.actionGuestToSignIn
import kotlinx.android.synthetic.main.fragment_guest_profile.*

class GuestProfileFragment(private val viewModel: GuestProfileViewModel) : BaseMVVMFragment(R.layout.fragment_guest_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAuthorizeProfileGuest.onClick { doNav(actionGuestToSignIn()) }
        itemRegionProfile.setOnClick { doNav(globalToRegion()) }
    }

    override fun onBindLiveData() {
        observe(viewModel.tempRegionLiveData) { itemRegionProfile.setDetailText(it?.name) }
    }
}