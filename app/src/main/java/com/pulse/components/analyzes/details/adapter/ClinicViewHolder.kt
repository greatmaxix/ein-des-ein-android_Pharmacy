package com.pulse.components.analyzes.details.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemClinicBinding

class ClinicViewHolder(
    itemView: View,
    private val orderService: ((Clinic) -> Unit)?,
    private val makeCall: (String) -> Unit,
    private val geoNav: (Clinic) -> Unit
) : BaseViewHolder<Clinic>(itemView) {

    private val binding by viewBinding(ItemClinicBinding::bind)

    override fun bind(item: Clinic) = with(binding) {
        ivClinic.loadGlideCircle(item.logo.url) // TODO set placeholder
        mtvName.text = item.name
        mtvAddress.text = item.location.address
        mtvPhone.text = getString(R.string.pharmacyPhoneWith, item.phone)
        mtvPhone.setDebounceOnClickListener { makeCall(item.phone) }
        mtvProductPrice.text = getString(R.string.price, item.servicePrice)
        orderService?.let {
            mtvProductPrice.visible()
            mbOrderService.visible()
            mbOrderService.setDebounceOnClickListener { it(item) }
        }
        fabLocation.setDebounceOnClickListener { geoNav(item) }
    }

    companion object {

        fun newInstance(parent: ViewGroup, orderService: ((Clinic) -> Unit)?, makeCall: (String) -> Unit, geoNav: (Clinic) -> Unit) =
            ClinicViewHolder(parent.inflate(R.layout.item_clinic), orderService, makeCall, geoNav)
    }
}