package com.pulse.components.user.address

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.backPress
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.text
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.databinding.FragmentAddressBinding

class AddressFragment(private val viewModel: AddressViewModel) : BaseMVVMFragment(R.layout.fragment_address) {

    private val binding by viewBinding(FragmentAddressBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        mbSave.setDebounceOnClickListener {
            if (viewCustomerAddress.validateFields()) {
                viewModel.saveAddress(DeliveryInfoOrderData(tilNote.text(), viewCustomerAddress.obtainAddress()))
                backPress()
            }
        }
    }

    override fun onBindLiveData() = with(binding) {
        observe(viewModel.addressLiveData, {
            it.addressOrderData?.let(viewCustomerAddress::setAddress)
            etNote.setText(it.comment)
            tilNote.isEndIconVisible = false
        })
    }
}