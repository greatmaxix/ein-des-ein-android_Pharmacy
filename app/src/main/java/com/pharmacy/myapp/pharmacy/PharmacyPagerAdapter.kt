package com.pharmacy.myapp.pharmacy

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pharmacy.myapp.pharmacy.list.PharmacyListFragment
import com.pharmacy.myapp.pharmacy.map.PharmacyMapFragment
import org.koin.core.KoinComponent
import org.koin.core.get

class PharmacyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), KoinComponent {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> get<PharmacyListFragment>()
        else -> get<PharmacyMapFragment>()
    }

}