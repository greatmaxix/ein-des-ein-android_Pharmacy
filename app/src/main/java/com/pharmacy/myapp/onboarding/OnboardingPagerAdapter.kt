package com.pharmacy.myapp.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.koin.core.KoinComponent
import org.koin.core.get

class OnboardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), KoinComponent {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> get<OnboardingRegionFragment>()
        else -> get<OnboardingAuthFragment>()
    }

}