package com.pulse.components.user.profile

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pulse.MainGraphDirections.Companion.globalToRegion
import com.pulse.MainGraphDirections.Companion.globalToStub
import com.pulse.R
import com.pulse.components.mercureService.MercureEventListenerService
import com.pulse.components.stub.model.StubType
import com.pulse.components.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToEdit
import com.pulse.components.user.profile.ProfileFragmentDirections.Companion.fromProfileToNotifications
import com.pulse.components.user.profile.ProfileFragmentDirections.Companion.fromProfileToPayments
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentProfileBinding

class ProfileFragment : BaseToolbarFragment<ProfileViewModel>(R.layout.fragment_profile, ProfileViewModel::class) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun initUI() = with(binding) {
        fabEditProfile.setDebounceOnClickListener { navController.navigate(actionFromProfileToEdit()) }

        llWishContainer.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_wish, null, R.id.nav_profile) }
        llAnalyzesContainer.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_analyzes, null, R.id.nav_profile) }
        llRecipesContainer.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_profile) }
        llOrderContainer.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_orders, null, R.id.nav_profile) }

        itemRegion.onClickDebounce { navController.navigate(globalToRegion()) }
        itemLanguage.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_language, null, R.id.nav_profile) }
        itemAddress.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_address, null, R.id.nav_profile) }
        itemNotification.onClickDebounce { navController.navigate(fromProfileToNotifications()) }
        itemMyInsurance.onClickDebounce { navController.navigate(globalToStub(StubType.INSURANCE_POLICY)) }
        itemPayment.onClickDebounce { navController.navigate(fromProfileToPayments()) }
        itemPrivileges.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_privileges_type, null, R.id.nav_profile) }

        itemCharity.onClickDebounce { navController.navigate(globalToStub(StubType.CHARITY)) }

        itemAbout.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_about, null, R.id.nav_profile) }
        itemHelp.onClickDebounce { navController.onNavDestinationSelected(R.id.nav_need_help, null, R.id.nav_profile) }
        itemLogout.onClickDebounce { showLogoutDialog() }
    }

    override fun onBindStates() = with(lifecycleScope) {
        with(binding) {
            observe(viewModel.customerInfoFlow) {
                mtvName.text = it?.name
                mtvPhone.text = it?.phone?.addPlusSignIfNeeded()?.formatPhone()
                itemRegion.detailText = it?.region?.regionName ?: ""
            }
            observe(viewModel.avatarState) {
                ivProfile.loadGlideDrawableByURL(it) {
                    placeholder(R.drawable.ic_avatar)
                    apply(RequestOptions.circleCropTransform())
                    skipMemoryCache(true)
                    transition(DrawableTransitionOptions.withCrossFade())
                }
            }
        }
    }

    private fun showLogoutDialog() = uiHelper.showDialog(getString(R.string.areYouSureToExit)) {
        cancelable = false
        positive = getString(R.string.exit)
        positiveAction = {
            viewModel.logout()
            if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
            }
        }
        negative = getString(R.string.cancel)
    }
}