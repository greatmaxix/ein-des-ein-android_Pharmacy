package com.pulse.components.user.profile.guest

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.globalToRegion
import com.pulse.R
import com.pulse.components.user.profile.guest.GuestProfileFragmentDirections.Companion.actionGuestToSignIn
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentGuestProfileBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class GuestProfileFragment(private val viewModel: GuestProfileViewModel) : BaseMVVMFragment(R.layout.fragment_guest_profile) {

    private val binding by viewBinding(FragmentGuestProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        mbAuthorize.setDebounceOnClickListener { doNav(actionGuestToSignIn()) }
        itemRegion.setDebounceOnClickListener { doNav(globalToRegion()) }
        itemAbout.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_about, null, R.id.nav_profile) }
        itemHelp.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_need_help, null, R.id.nav_profile) }
    }

    override fun onBindLiveData() {
        observe(viewModel.tempRegionLiveData) { binding.itemRegion.detailText = it?.name ?: "" }
    }
}