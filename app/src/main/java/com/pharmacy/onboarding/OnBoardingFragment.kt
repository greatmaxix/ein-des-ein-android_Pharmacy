package com.pharmacy.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.onboarding.adapter.OnBoardingPagerAdapter
import com.pharmacy.region.RegionFragment.Companion.REGION_SELECTION_FINISHED_KEY
import kotlinx.android.synthetic.main.fragment_onboarding.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

class OnBoardingFragment : BaseMVVMFragment(R.layout.fragment_onboarding) {

    private val vm: OnBoardingViewModel by viewModel()

    @KoinApiExtension
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vpOnboarding.adapter = OnBoardingPagerAdapter(this)
        setFragmentResultListener(REGION_SELECTION_FINISHED_KEY) { _, _ -> vm.skipRegion(true) }
        attachBackPressCallback {
            if (vpOnboarding.currentItem == 1) {
                vpOnboarding.currentItem = 0
            } else navigationBack()
        }
    }

    override fun onBindLiveData() {
        observe(vm.skipRegionLiveData, ::goToAuthPage)
        observe(vm.directionLiveData, navController::navigate)
    }

    private fun goToAuthPage(unit: Unit) {
        vpOnboarding.currentItem = 1
    }
}