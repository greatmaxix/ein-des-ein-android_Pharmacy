package com.pharmacy.myapp.user.profile

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.myapp.MainGraphDirections
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToEdit
import com.pharmacy.myapp.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToWish
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseMVVMFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabEditProfile.onClick { doNav(actionFromProfileToEdit()) }
        itemRegionProfile.setOnClick { doNav(MainGraphDirections.globalToRegion()) }
        itemLogoutProfile.setOnClick { showLogoutDialog() }

        wishContainerProfile.onClick { doNav(actionFromProfileToWish()) }
        analyzesContainerProfile.onClick { navController.onNavDestinationSelected(R.id.nav_analyzes, null, R.id.nav_profile) }
        recipesContainerProfile.onClick { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_profile) }
        orderContainerProfile.onClick { navController.onNavDestinationSelected(R.id.nav_my_orders, null, R.id.nav_profile) }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.customerInfoLiveData.observeExt {
            mtvNameProfile.text = it.name
            mtvPhoneProfile.text = it.phone.addPlusSignIfNeeded().formatPhone()
        }
        viewModel.directionLiveData.observeExt(navController::navigate)
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
        viewModel.avatarLiveData.observeNullableExt {
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
        positive = R.string.common_okButton
        positiveAction = { viewModel.logout() }
        negative = R.string.common_closeButton
    }

}