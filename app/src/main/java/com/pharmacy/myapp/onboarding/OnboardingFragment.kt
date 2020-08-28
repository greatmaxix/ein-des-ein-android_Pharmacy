package com.pharmacy.myapp.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.region.RegionFragment.Companion.REGION_SELECTION_FINISHED_KEY
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : BaseMVVMFragment(R.layout.fragment_onboarding) {

    private val viewModel: OnboardingViewModel by sharedGraphViewModel(R.id.onboarding_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vpOnboarding.adapter = OnboardingPagerAdapter(this)
        clearFragmentResultListener(REGION_SELECTION_FINISHED_KEY)
        setFragmentResultListener(REGION_SELECTION_FINISHED_KEY) { _, _ -> viewModel.skipRegion(true) }
        attachBackPressCallback {
            if (vpOnboarding.currentItem == 1) {
                vpOnboarding.currentItem = 0
            } else navigationBack()
        }
    }

    override fun onBindLiveData() {
        viewModel.skipRegionLiveData.observeExt(::goToAuthPage)
        viewModel.directionLiveData.observeExt(navController::navigate)
    }

    private fun goToAuthPage(unit: Unit) = vpOnboarding.setCurrentItem(1)

}