package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.data.remote.model.order.AddressOrderData
import com.pulse.databinding.ViewBuyerDeliverAddressOrderBinding

class BuyerDeliveryAddressOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBuyerDeliverAddressOrderBinding.inflate(inflater, this)
    private val sidePadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    var deliveryAddress: AddressOrderData? = null
        set(value) {
            if (value != null) {
                field = value
                updateFieldsContent(field)
            }
        }

    init {
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
        orientation = VERTICAL
    }

    private fun updateFieldsContent(address: AddressOrderData? = null) = with(address) {
        val cityAndStreetHolder = "\uD83C\uDFE0 ${this?.streetAndCity}"
        binding.mtvPharmacyAddress.text = cityAndStreetHolder
        val houseAndApartmentHolder = "\uD83D\uDEAA ${this?.houseAndApartment}"
        binding.mtvBuyerAddressDetail.text = houseAndApartmentHolder
    }
}