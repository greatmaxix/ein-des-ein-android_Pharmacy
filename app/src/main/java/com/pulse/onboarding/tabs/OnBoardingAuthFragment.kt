package com.pulse.onboarding.tabs

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.onClick
import com.pulse.onboarding.OnBoardingViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_auth.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class OnBoardingAuthFragment : BaseMVVMFragment(R.layout.fragment_onboarding_auth) {

    private val vm: OnBoardingViewModel by lazy { requireParentFragment().getViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvOnboardingSkipAuth.onClick { vm.goToHome() }
        generalInfoAuth.setButtonAction { vm.goToAuth() }
    }

}