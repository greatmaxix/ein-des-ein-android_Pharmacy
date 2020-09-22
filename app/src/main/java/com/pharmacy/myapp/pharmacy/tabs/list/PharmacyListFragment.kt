package com.pharmacy.myapp.pharmacy.tabs.list

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.core.extensions.showDial
import com.pharmacy.myapp.core.extensions.showDirection
import com.pharmacy.myapp.pharmacy.tabs.BaseTabFragment
import com.pharmacy.myapp.pharmacy.tabs.list.adapter.PharmacyListAdapter
import kotlinx.android.synthetic.main.fragment_pharmacy_list.*


class PharmacyListFragment : BaseTabFragment(R.layout.fragment_pharmacy_list) {

    private val pharmacyAdapter =
        PharmacyListAdapter({ addProductToCart(it.pharmacyProducts.first().pharmacyProductId) }, ::showDial, { showDirection(it.location.lat, it.location.lng) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(rvPharmacy) {
            adapter = pharmacyAdapter
            addItemDecorator()
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.pharmacyListLiveData, pharmacyAdapter::notifyItems)
    }
}