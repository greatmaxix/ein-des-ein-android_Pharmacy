package com.pulse.components.onboarding

import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToAuth
import com.pulse.components.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToHome
import com.pulse.components.onboarding.OnboardingFragmentDirections.Companion.actionFromOnboardingToRegion
import com.pulse.components.onboarding.adapter.OnboardingPagerAdapter
import com.pulse.components.onboarding.model.Onboarding
import com.pulse.components.region.RegionFragment.Companion.REGION_KEY
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.doWithDelay
import com.pulse.databinding.FragmentOnboardingBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OnboardingFragment : BaseMVVMFragment<OnboardingViewModel>(R.layout.fragment_onboarding, OnboardingViewModel::class) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
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

    override fun initUI() = with(binding) {
        viewModel.setRegionSelectionFlag()

        with(vpOnboarding) {
            isUserInputEnabled = false
            adapter = OnboardingPagerAdapter(items, ::onSkip, ::onNext)
            offscreenPageLimit = 2
            attachBackPressCallback { if (currentItem == AUTH) currentItem = REGION else navigationBack() }
            observeSavedStateHandler<Boolean>(REGION_KEY) {
                doWithDelay(500) { currentItem = 1 }
            }
        }
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
        binding.vpOnboarding.currentItem = AUTH
    }

    companion object {

        private const val REGION = 0
        private const val AUTH = 1
    }
}