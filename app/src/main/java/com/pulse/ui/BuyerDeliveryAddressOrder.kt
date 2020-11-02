package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflate
import com.pulse.data.remote.model.order.AddressOrderData
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_deliver_address_order.view.*

class BuyerDeliveryAddressOrder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_deliver_address_order, true)

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
        tvPharmacyAddressOrder.text = cityAndStreetHolder
        val houseAndApartmentHolder = "\uD83D\uDEAA ${this?.houseAndApartment}"
        tvBuyerAddressDetailOrder.text = houseAndApartmentHolder
    }

}