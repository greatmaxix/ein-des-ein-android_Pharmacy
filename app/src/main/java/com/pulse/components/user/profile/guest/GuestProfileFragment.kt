package com.pulse.components.user.profile.guest

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.globalToRegion
import com.pulse.R
import com.pulse.components.user.profile.guest.GuestProfileFragmentDirections.Companion.actionGuestToSignIn
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentGuestProfileBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class GuestProfileFragment : BaseToolbarFragment<GuestProfileViewModel>(R.layout.fragment_guest_profile, GuestProfileViewModel::class) {

    private val binding by viewBinding(FragmentGuestProfileBinding::bind)

    override fun initUI() = with(binding) {
        mbAuthorize.setDebounceOnClickListener { navController.navigate(actionGuestToSignIn()) }
        itemRegion.setDebounceOnClickListener { navController.navigate(globalToRegion()) }
        itemAbout.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_about, null, R.id.nav_profile) }
        itemHelp.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_need_help, null, R.id.nav_profile) }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.tempRegionFlow) { binding.itemRegion.detailText = it?.name ?: "" }
    }
}