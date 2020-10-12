package com.pharmacy.myapp.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.onboarding.adapter.OnBoardingPagerAdapter
import com.pharmacy.myapp.region.RegionFragment.Companion.REGION_SELECTION_FINISHED_KEY
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