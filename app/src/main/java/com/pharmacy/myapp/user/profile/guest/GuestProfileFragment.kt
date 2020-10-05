package com.pharmacy.myapp.user.profile.guest

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToRegion
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onNavDestinationSelected
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.user.profile.guest.GuestProfileFragmentDirections.Companion.actionGuestToSignIn
import kotlinx.android.synthetic.main.fragment_guest_profile.*

class GuestProfileFragment(private val viewModel: GuestProfileViewModel) : BaseMVVMFragment(R.layout.fragment_guest_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAuthorizeProfileGuest.setDebounceOnClickListener { doNav(actionGuestToSignIn()) }
        itemRegionProfile.setDebounceOnClickListener { doNav(globalToRegion()) }
        itemAboutProfile.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_about, null, R.id.nav_profile) }
    }

    override fun onBindLiveData() {
        observe(viewModel.tempRegionLiveData) { itemRegionProfile.setDetailText(it?.name) }
    }
}