package com.pulse.components.user.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pulse.MainGraphDirections.Companion.globalToRegion
import com.pulse.R
import com.pulse.components.mercureService.MercureEventListenerService
import com.pulse.components.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToEdit
import com.pulse.components.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToWish
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentProfileBinding

class ProfileFragment : BaseMVVMFragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        fabEditProfile.setDebounceOnClickListener { doNav(actionFromProfileToEdit()) }
        itemRegion.setDebounceOnClickListener { doNav(globalToRegion()) }
        itemLogout.setDebounceOnClickListener { showLogoutDialog() }
        llWishContainer.setDebounceOnClickListener { doNav(actionFromProfileToWish()) }
        llAnalyzesContainer.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_analyzes, null, R.id.nav_profile) }
        llRecipesContainer.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_profile) }
        llOrderContainer.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_orders, null, R.id.nav_profile) }
        itemAbout.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_about, null, R.id.nav_profile) }
        itemAddress.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_address, null, R.id.nav_profile) }
        itemHelp.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_need_help, null, R.id.nav_profile) }
        itemPayment.mockToast()
        itemNotification.mockToast()
    }

    override fun onBindLiveData() = with(binding) {
        super.onBindLiveData()
        observe(viewModel.customerInfoLiveData) {
            mtvName.text = it?.name
            mtvPhone.text = it?.phone?.addPlusSignIfNeeded()?.formatPhone()
            itemRegion.detailText = it?.region?.regionName ?: ""
        }
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observeNullable(viewModel.avatarLiveData) {
            ivProfile.loadGlideDrawableByURL(it) {
                placeholder(R.drawable.ic_avatar)
                apply(RequestOptions.circleCropTransform())
                skipMemoryCache(true)
                transition(DrawableTransitionOptions.withCrossFade())
            }
        }
    }

    private fun showLogoutDialog() = showAlertRes(getString(R.string.areYouSureToExit)) {
        cancelable = false
        positive = R.string.exit
        positiveAction = {
            viewModel.logout()
            if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
            }
        }
        negative = R.string.cancel
    }
}