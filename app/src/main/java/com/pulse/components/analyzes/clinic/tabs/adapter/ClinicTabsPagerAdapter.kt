package com.pulse.components.analyzes.clinic.tabs.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pulse.components.analyzes.clinic.tabs.list.ClinicListFragment
import com.pulse.components.analyzes.clinic.tabs.map.ClinicMapFragment
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@KoinApiExtension
class ClinicTabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment), KoinComponent {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> get<ClinicListFragment>()
        else -> get<ClinicMapFragment>()
    }
}