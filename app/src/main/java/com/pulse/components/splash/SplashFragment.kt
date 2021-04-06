package com.pulse.components.splash

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.splash.adapter.SplashPagerAdapter
import com.pulse.components.splash.model.Splash
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.visible
import com.pulse.databinding.FragmentSplashBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SplashFragment : BaseMVVMFragment<SplashViewModel>(R.layout.fragment_splash, SplashViewModel::class) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val items = listOf(
        Splash("onboarding_1", R.string.onboarding_title_1, R.string.onboarding_subtitle_1),
        Splash("onboarding_2", R.string.onboarding_title_2, R.string.onboarding_subtitle_2),
        Splash("onboarding_3", R.string.onboarding_title_3, R.string.onboarding_subtitle_3)
    )

    override fun initUI() {
        viewModel.updateCustomer()
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.onboardingState, ::onboardingOrRegionsOrMain)
    }

    private fun onboardingOrRegionsOrMain(isOnboarding: Boolean) {
        if (isOnboarding) showOnboarding() else viewModel.regionsOrMain()
    }

    private fun showOnboarding() = with(binding.vpSplash) {
        adapter = SplashPagerAdapter(
            items,
            { viewModel.notifyOnboarding() },
            { if (currentItem == 2) viewModel.notifyOnboarding() else setCurrentItem(currentItem + 1, true) }
        )
        offscreenPageLimit = 3
        visible()
    }
}