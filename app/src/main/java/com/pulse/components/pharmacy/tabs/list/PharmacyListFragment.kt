package com.pulse.components.pharmacy.tabs.list

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.pharmacy.tabs.BaseTabFragment
import com.pulse.components.pharmacy.tabs.list.adapter.PharmacyListAdapter
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.showDirection
import com.pulse.databinding.FragmentPharmacyListBinding

class PharmacyListFragment : BaseTabFragment(R.layout.fragment_pharmacy_list) {

    private val binding by viewBinding(FragmentPharmacyListBinding::bind)
    private val pharmacyAdapter = PharmacyListAdapter(
        { it.pharmacyProducts?.first()?.pharmacyProductId?.let(::addProduct) },
        ::showDial,
        { showDirection(it.location.lat, it.location.lng) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding.rvPharmacy) {
        super.onViewCreated(view, savedInstanceState)

        adapter = pharmacyAdapter
        addItemDecorator()
    }

    override fun onBindLiveData() {
        observeResult(viewModel.pharmacyListLiveData) {
            onEmmit = { pharmacyAdapter.notifyItems(this) }
        }
    }
}