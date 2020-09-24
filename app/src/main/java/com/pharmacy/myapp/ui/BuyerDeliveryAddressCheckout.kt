package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempDeliveryAddress
import com.pharmacy.myapp.core.extensions.inflate
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

    private var deliveryAddress: TempDeliveryAddress = TempDeliveryAddress.newInstance()

    init {
        tilCityCheckout.editText?.doAfterTextChanged {
            deliveryAddress.city = it.toString()
            clearError()
        }
        tilStreetCheckout.editText?.doAfterTextChanged {
            deliveryAddress.street = it.toString()
            clearError()
        }
        tilHouseNoCheckout.editText?.doAfterTextChanged {
            deliveryAddress.houseNo = it.toString()
            clearError()
        }
        tilApartmentNoCheckout.editText?.doAfterTextChanged { deliveryAddress.apartmentNo = it.toString() }
        orientation = VERTICAL
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
    }

    private fun clearError() {
        tilCityCheckout.isErrorEnabled = false
        tilStreetCheckout.isErrorEnabled = false
        tilHouseNoCheckout.isErrorEnabled = false
    }

    fun setData(deliveryAddress: TempDeliveryAddress? = null) {
        deliveryAddress?.let {
            this.deliveryAddress = it
            updateFieldsContent()
        }
    }

    private fun updateFieldsContent() {
        with(deliveryAddress) {
            tilCityCheckout.editText?.setText(city)
            tilStreetCheckout.editText?.setText(street)
            tilHouseNoCheckout.editText?.setText(houseNo)
            tilApartmentNoCheckout.editText?.setText(apartmentNo)
        }
    }

    fun validateFields() = tilCityCheckout.isAddressLengthValid() && tilStreetCheckout.isAddressLengthValid() && tilHouseNoCheckout.isAddressLengthValid()

    fun obtainDeliveryAddress() = deliveryAddress
}