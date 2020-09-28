package com.pharmacy.myapp.ui

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.data.remote.rest.request.order.AddressOrderData
import com.pharmacy.myapp.ui.text.isAddressLengthValid
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_deliver_address_checkout.view.*

class BuyerDeliveryAddressCheckout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_deliver_address_checkout, true)

    private val sidePadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    private val deliveryAddress by lazy { AddressOrderData() }

    init {
        tilCityCheckout.editText?.doAfterTextChanged(assignWithClearError { deliveryAddress.city = toString() })
        tilStreetCheckout.editText?.doAfterTextChanged(assignWithClearError { deliveryAddress.street = toString() })
        tilHouseNoCheckout.editText?.doAfterTextChanged(assignWithClearError { deliveryAddress.house = toString() })
        tilApartmentNoCheckout.editText?.doAfterTextChanged { deliveryAddress.apartment = it.toString() }
        orientation = VERTICAL
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
    }

    private fun assignWithClearError(returnedValue: Editable?.() -> Unit) = { editable: Editable? ->
        clearError()
        returnedValue(editable)
    }

    private fun clearError() {
        tilCityCheckout.isErrorEnabled = false
        tilStreetCheckout.isErrorEnabled = false
        tilHouseNoCheckout.isErrorEnabled = false
    }

    fun validateFields(): Boolean {
        val isValid = tilCityCheckout.isAddressLengthValid() && tilStreetCheckout.isAddressLengthValid() && tilHouseNoCheckout.isAddressLengthValid()
        if(!isValid) requestFocus()
        return isValid
    }

    fun obtainDeliveryAddress() = deliveryAddress
}