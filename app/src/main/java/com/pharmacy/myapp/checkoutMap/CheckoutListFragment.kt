package com.pharmacy.myapp.checkoutMap

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import kotlinx.android.synthetic.main.fragment_checkout_list.*

class CheckoutListFragment : BaseMVVMFragment(R.layout.fragment_checkout_list) {

    private val viewModel: CheckoutMapViewModel by sharedGraphViewModel(R.id.checkout_map_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAvailableDrugstores.layoutManager = LinearLayoutManager(context)
        rvAvailableDrugstores.addItemDecorator()
    }

    override fun onBindLiveData() {
        viewModel.drugstoresLiveData.observeExt {
            rvAvailableDrugstores.adapter = AvailableDrugstoresAdapter(it) {
                viewModel.setSelectedDrugstore(it)
            }
        }
    }

}