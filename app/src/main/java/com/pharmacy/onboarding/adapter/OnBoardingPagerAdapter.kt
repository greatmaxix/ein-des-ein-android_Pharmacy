package com.pharmacy.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pharmacy.onboarding.tabs.OnBoardingAuthFragment
import com.pharmacy.onboarding.tabs.OnBoardingRegionFragment
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class OnBoardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), KoinComponent {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> get<OnBoardingRegionFragment>()
        else -> get<OnBoardingAuthFragment>()
    }

}