package com.pharmacy.myapp.checkoutMap

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.data.DummyData
import kotlinx.android.synthetic.main.fragment_checkout_list.*

class CheckoutListFragment : BaseMVVMFragment(R.layout.fragment_checkout_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAvailableDrugstores.layoutManager = LinearLayoutManager(context)
        rvAvailableDrugstores.adapter = AvailableDrugstoresAdapter(DummyData.getAvailableDrugstores())
        rvAvailableDrugstores.addItemDecorator()
    }

}