package com.pharmacy.myapp.pharmacy.list

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.core.extensions.showDial
import com.pharmacy.myapp.core.extensions.showDirection
import com.pharmacy.myapp.pharmacy.PharmacyViewModel
import com.pharmacy.myapp.pharmacy.list.adapter.PharmacyListAdapter
import kotlinx.android.synthetic.main.fragment_pharmacy_list.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class PharmacyListFragment : BaseMVVMFragment(R.layout.fragment_pharmacy_list) {

    private val viewModel: PharmacyViewModel by lazy { requireParentFragment().getViewModel() }

    private val pharmacyAdapter = PharmacyListAdapter({

    }, { showDial(it) }, { showDirection(it.location.lat, it.location.lng) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(rvPharmacy) {
            adapter = pharmacyAdapter
            addItemDecorator()
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.pharmacyListLiveData, pharmacyAdapter::notifyItems)
    }
}