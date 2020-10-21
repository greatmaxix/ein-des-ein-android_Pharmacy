package com.pulse.onboarding.tabs

import android.Manifest
import android.os.Bundle
import android.view.View
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.MainGraphDirections.Companion.globalToRegion
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.onClick
import com.pulse.onboarding.OnBoardingViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_region.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class OnBoardingRegionFragment : BaseMVVMFragment(R.layout.fragment_onboarding_region) {

    private val vm: OnBoardingViewModel by lazy { requireParentFragment().getViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvOnboardingSkipRegion.onClick { vm.skipRegion() }
        generalInfoRegion.setButtonAction {
            val request = permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build()
            request.addListener { doNav(globalToRegion()) }
            request.send()
        }
    }

}