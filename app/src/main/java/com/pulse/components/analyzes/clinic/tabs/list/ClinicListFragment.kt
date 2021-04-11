package com.pulse.components.analyzes.clinic.tabs.list

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.clinic.tabs.BaseClinicTabFragment
import com.pulse.components.analyzes.clinic.tabs.ClinicTabsFragmentDirections.Companion.globalToClinicCard
import com.pulse.components.analyzes.details.adapter.ClinicsAdapter
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.showDirection
import com.pulse.databinding.FragmentClinicsListBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicListFragment : BaseClinicTabFragment(R.layout.fragment_clinics_list) {

    private val binding by viewBinding(FragmentClinicsListBinding::bind)
    private val clinicsAdapter by lazy {
        ClinicsAdapter(
            { navController.navigate(globalToClinicCard(it)) },
            viewModel::orderService,
            ::showDial,
            { showDirection(it.location.lat, it.location.lng) }
        )
    }

    override fun initUI() = with(binding.rvClinics) {
        adapter = clinicsAdapter
        addItemDecorator()
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.clinicsListState, clinicsAdapter::notifyItems)
    }
}