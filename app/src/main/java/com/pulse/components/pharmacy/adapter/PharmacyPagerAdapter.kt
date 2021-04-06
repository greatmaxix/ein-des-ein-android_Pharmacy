package com.pulse.components.pharmacy.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pulse.components.pharmacy.tabs.list.PharmacyListFragment
import com.pulse.components.pharmacy.tabs.map.PharmacyMapFragment
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class PharmacyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), KoinComponent {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> get<PharmacyListFragment>()
        else -> get<PharmacyMapFragment>()
    }
}