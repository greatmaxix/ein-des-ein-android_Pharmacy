package com.pharmacy.myapp.onboarding

import android.Manifest
import android.os.Bundle
import android.view.View
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_region.*

class OnboardingRegionFragment : BaseMVVMFragment(R.layout.fragment_onboarding_region) {

    private val viewModel: OnboardingViewModel by sharedGraphViewModel(R.id.onboarding_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvOnboardingSkipRegion.onClick { viewModel.skipRegion() }
        generalInfoRegion.setButtonAction {
            val request = permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build()
            request.addListener { _ ->
                // todo go to region selection
                viewModel.skipRegion()
            }
            request.send()
        }
    }

}