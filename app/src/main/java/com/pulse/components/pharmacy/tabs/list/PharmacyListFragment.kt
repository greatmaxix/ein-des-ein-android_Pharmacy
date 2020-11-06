package com.pulse.components.pharmacy.tabs.list

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.components.pharmacy.tabs.BaseTabFragment
import com.pulse.components.pharmacy.tabs.list.adapter.PharmacyListAdapter
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.showDial
import com.pulse.core.extensions.showDirection
import kotlinx.android.synthetic.main.fragment_pharmacy_list.*


class PharmacyListFragment : BaseTabFragment(R.layout.fragment_pharmacy_list) {

    private val pharmacyAdapter =
        PharmacyListAdapter({
            it.pharmacyProducts?.first()?.pharmacyProductId?.let(::addProduct)
        }, ::showDial, { showDirection(it.location.lat, it.location.lng) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(rvPharmacy) {
            adapter = pharmacyAdapter
            addItemDecorator()
        }
    }

    override fun onBindLiveData() {
        observeResult(vm.pharmacyListLiveData) {
            onEmmit = {
                pharmacyAdapter.notifyItems(this)
            }
        }
    }
}