package com.pharmacy.ui

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.R
import com.pharmacy.core.extensions.inflate
import com.pharmacy.data.remote.model.order.AddressOrderData
import com.pharmacy.ui.text.isAddressLengthValid
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_deliver_address.view.*

class BuyerDeliveryAddress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_deliver_address, true)

    private val sidePadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    private val address by lazy { AddressOrderData() }

    init {
        tilCityAddress.editText?.doAfterTextChanged(assignWithClearError { address.city = toString() })
        tilStreetAddress.editText?.doAfterTextChanged(assignWithClearError { address.street = toString() })
        tilHouseAddress.editText?.doAfterTextChanged(assignWithClearError { address.house = toString() })
        tilApartmentAddress.editText?.doAfterTextChanged { address.apartment = it.toString() }
        orientation = VERTICAL
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
    }

    private fun assignWithClearError(action: Editable?.() -> Unit) = { text: Editable? ->
        clearError()
        action(text)
    }

    private fun clearError() {
        tilCityAddress.isErrorEnabled = false
        tilStreetAddress.isErrorEnabled = false
        tilHouseAddress.isErrorEnabled = false
    }

    fun validateFields(): Boolean {
        val isValid = tilCityAddress.isAddressLengthValid() && tilStreetAddress.isAddressLengthValid() && tilHouseAddress.isAddressLengthValid()
        if (!isValid) requestFocus()
        return isValid
    }

    fun obtainAddress() = address

    fun setAddress(data: AddressOrderData) = with(data) {
        etCityAddress.setText(city)
        etStreetAddress.setText(street)
        etHouseAddress.setText(house)
        etApartmentAddress.setText(apartment)
        tilCityAddress.isEndIconVisible = false
        tilStreetAddress.isEndIconVisible = false
        tilHouseAddress.isEndIconVisible = false
        tilApartmentAddress.isEndIconVisible = false
    }
}