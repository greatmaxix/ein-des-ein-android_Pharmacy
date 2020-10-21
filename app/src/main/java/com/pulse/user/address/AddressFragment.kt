package com.pulse.user.address

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.backPress
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.text
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
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
            tilNoteAddress.isEndIconVisible = false
        })
    }

}