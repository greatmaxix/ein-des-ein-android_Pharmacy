package com.pharmacy.onboarding.tabs

import android.Manifest
import android.os.Bundle
import android.view.View
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.MainGraphDirections.Companion.globalToRegion
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.onClick
import com.pharmacy.onboarding.OnBoardingViewModel
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