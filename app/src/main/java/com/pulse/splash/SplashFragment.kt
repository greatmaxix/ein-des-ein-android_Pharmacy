package com.pulse.splash

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.navigation.NavDirections
import com.pulse.R
import com.pulse.components.onboarding.model.Onboarding
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.setNavigationBarColor
import com.pulse.core.extensions.setStatusBarColor
import com.pulse.core.extensions.visible
import com.pulse.core.general.Event
import com.pulse.splash.adapter.OnboardingPagerAdapter
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashFragment(private val viewModel: SplashViewModel) : BaseMVVMFragment(R.layout.fragment_splash) {

    private val items = listOf(
        Onboarding("onboarding_1", R.string.onboarding_title_1, R.string.onboarding_subtitle_1),
        Onboarding("onboarding_2", R.string.onboarding_title_2, R.string.onboarding_subtitle_2),
        Onboarding("onboarding_3", R.string.onboarding_title_3, R.string.onboarding_subtitle_3)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateCustomer()
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, ::moveByDirection)
        observe(viewModel.onboardingLiveData, ::onboardingOrRegionsOrMain)
    }

    private fun moveByDirection(event: Event<NavDirections>) {
        event.contentOrNull?.let(navController::navigate)
    }

    private fun onboardingOrRegionsOrMain(isOnboarding: Boolean) {
        if (isOnboarding) showOnboarding() else viewModel.regionsOrMain()
    }

    private fun showOnboarding() {
        changeBarsColor(R.color.darkBlue)
        with(vpSplash) {
            adapter =
                OnboardingPagerAdapter(items, { viewModel.notifyOnboarding() }, { if (currentItem == 2) viewModel.notifyOnboarding() else setCurrentItem(currentItem + 1, true) })
            offscreenPageLimit = 3
            visible()
        }
    }

    private fun changeBarsColor(@ColorRes colorRes: Int) {
        setStatusBarColor(colorRes)
        setNavigationBarColor(colorRes)
    }
}