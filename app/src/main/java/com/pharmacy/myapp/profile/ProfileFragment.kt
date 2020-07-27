package com.pharmacy.myapp.profile

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addPlusSignIfNeeded
import com.pharmacy.myapp.core.extensions.formatPhone
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.profile.ProfileFragmentDirections.Companion.actionFromProfileToEdit
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseMVVMFragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabEditProfile.onClick { doNav(actionFromProfileToEdit()) }
        itemLogoutProfile.setOnClick { viewModel.logout() }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.customerInfoLiveData.observeExt {
            mtvNameProfile.text = it.name
            mtvPhoneProfile.text = it.phone?.addPlusSignIfNeeded()?.formatPhone()
        }
        viewModel.directionLiveData.observeExt(navController::navigate)
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
        viewModel.avatarLiveData.observeNullableExt {
            Glide.with(this)
                .load(it)
                .apply(RequestOptions.circleCropTransform())
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivProfile)
        }

    }

}