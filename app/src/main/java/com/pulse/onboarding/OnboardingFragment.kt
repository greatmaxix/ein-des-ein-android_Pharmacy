package com.pulse.onboarding

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.data.local.SPManager
import com.pulse.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToAuth
import com.pulse.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToHome
import com.pulse.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToRegion
import com.pulse.onboarding.adapter.OnboardingPagerAdapter
import com.pulse.onboarding.model.Onboarding
import com.pulse.region.RegionFragment.Companion.REGION_KEY
import kotlinx.android.synthetic.main.fragment_onboarding.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OnboardingFragment(sp: SPManager) : BaseMVVMFragment(R.layout.fragment_onboarding) {

    init {
        sp.isNeedRegionSelection = true
    }

    private val items = listOf(
        Onboarding(
            R.drawable.ic_onboarding_region,
            R.string.onboardingRegionTitle,
            R.string.onboardingRegionDescription,
            R.string.onboardingRegionActionText,
            R.string.skip,
            Onboarding.OnboardingType.REGION
        ),
        Onboarding(
            R.drawable.ic_onboarding_buy,
            R.string.onboardingAuthTitle,
            R.string.onboardingAuthDescription,
            R.string.onboardingAuthAction,
            R.string.onboardingAuthSkip,
            Onboarding.OnboardingType.AUTH
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vpOnboarding) {
            adapter = OnboardingPagerAdapter(items, ::onSkip, ::onNext)
            offscreenPageLimit = 2

            attachBackPressCallback { if (currentItem == AUTH) currentItem = REGION else navigationBack() }
        }
    }

    override fun onBindLiveData() {
        observeSavedStateHandler<Boolean>(REGION_KEY) { moveToAuth() }
    }

    private fun onSkip(type: Onboarding.OnboardingType) = when (type) {
        Onboarding.OnboardingType.AUTH -> navController.navigate(actionFromOnboardingToHome())
        Onboarding.OnboardingType.REGION -> moveToAuth()
    }

    private fun onNext(type: Onboarding.OnboardingType) = when (type) {
        Onboarding.OnboardingType.AUTH -> navController.navigate(actionFromOnboardingToAuth())
        Onboarding.OnboardingType.REGION -> navController.navigate(actionFromOnboardingToRegion())
    }

    private fun moveToAuth() {
        vpOnboarding.currentItem = AUTH
    }

    companion object {
        private const val REGION = 0
        private const val AUTH = 1
    }
}