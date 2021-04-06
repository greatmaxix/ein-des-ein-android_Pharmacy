package com.pulse.components.user.address

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.backPress
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.text
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.databinding.FragmentAddressBinding

class AddressFragment : BaseToolbarFragment<AddressViewModel>(R.layout.fragment_address, AddressViewModel::class) {

    private val binding by viewBinding(FragmentAddressBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        mbSave.setDebounceOnClickListener {
            if (viewCustomerAddress.validateFields()) {
                viewModel.saveAddress(DeliveryInfoOrderData(tilNote.text(), viewCustomerAddress.obtainAddress()))
                backPress()
            }
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        with(binding) {
            observe(viewModel.addressFlow) {
                it?.let {
                    it.addressOrderData?.let(viewCustomerAddress::setAddress)
                    etNote.setText(it.comment)
                    tilNote.isEndIconVisible = false
                }
            }
        }
    }
}