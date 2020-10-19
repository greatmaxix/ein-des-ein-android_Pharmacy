package com.pharmacy.myapp.user.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToRegion
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.mercureService.MercureEventListenerService
import com.pharmacy.myapp.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToEdit
import com.pharmacy.myapp.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToWish
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseMVVMFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabEditProfile.setDebounceOnClickListener { doNav(actionFromProfileToEdit()) }
        itemRegionProfile.setDebounceOnClickListener { doNav(globalToRegion()) }
        itemLogoutProfile.setDebounceOnClickListener { showLogoutDialog() }

        wishContainerProfile.setDebounceOnClickListener { doNav(actionFromProfileToWish()) }
        analyzesContainerProfile.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_analyzes, null, R.id.nav_profile) }
        recipesContainerProfile.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_profile) }
        orderContainerProfile.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_orders, null, R.id.nav_profile) }
        itemAboutProfile.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_about, null, R.id.nav_profile) }
        itemAddressProfile.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_address, null, R.id.nav_profile) }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.customerInfoLiveData) {
            mtvNameProfile.text = it?.name
            mtvPhoneProfile.text = it?.phone?.addPlusSignIfNeeded()?.formatPhone()
            itemRegionProfile.setDetailText(it?.region?.regionName ?: "")
        }
        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observeNullable(viewModel.avatarLiveData) {
            ivProfile.loadGlide(it) {
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