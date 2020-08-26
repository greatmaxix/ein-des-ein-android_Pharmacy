package com.pharmacy.myapp.onboarding

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment: BaseMVVMFragment(R.layout.fragment_onboarding) {

    private val viewModel: OnboardingViewModel by sharedGraphViewModel(R.id.onboarding_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vpOnboarding.adapter = OnboardingPagerAdapter(this)
    }

    override fun onBindLiveData() {
        viewModel.skipRegionLiveData.observeExt {
            if (vpOnboarding.currentItem != 1) vpOnboarding.currentItem = 1
        }
        viewModel.directionLiveData.observeExt(navController::navigate)
    }

}