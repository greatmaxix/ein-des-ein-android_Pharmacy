package com.pharmacy.myapp.onboarding

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_auth.*

class OnboardingAuthFragment: BaseMVVMFragment(R.layout.fragment_onboarding_auth) {

    private val viewModel: OnboardingViewModel by sharedGraphViewModel(R.id.onboarding_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvOnboardingSkipAuth.onClick { viewModel.goToHome() }
        generalInfoAuth.setButtonAction { viewModel.goToAuth() }
    }


}