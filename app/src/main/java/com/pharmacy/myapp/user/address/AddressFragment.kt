package com.pharmacy.myapp.user.address

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.backPress
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.core.extensions.text
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData
import kotlinx.android.synthetic.main.fragment_address.*

class AddressFragment(private val viewModel: AddressViewModel) : BaseMVVMFragment(R.layout.fragment_address) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        btnSaveAddress.setDebounceOnClickListener {
            if (customerAddress.validateFields()) {
                viewModel.saveAddress(DeliveryInfoOrderData(tilNoteAddress.text(), customerAddress.obtainAddress()))
                backPress()
            }
        }
    }

    override fun onBindLiveData() {
        observe(viewModel.addressLiveData, {
            it.addressOrderData?.let(customerAddress::setAddress)
            etNoteAddress.setText(it.comment)
        })
    }

}