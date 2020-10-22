package com.pulse.user.profile.guest

import android.os.Bundle
import android.view.View
import com.pulse.MainGraphDirections.Companion.globalToRegion
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.user.profile.guest.GuestProfileFragmentDirections.Companion.actionGuestToSignIn
import kotlinx.android.synthetic.main.fragment_guest_profile.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
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