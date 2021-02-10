package com.pulse.ui

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.data.remote.model.order.AddressOrderData
import com.pulse.databinding.ViewBuyerDeliverAddressBinding
import com.pulse.ui.text.isAddressLengthValid

class BuyerDeliveryAddress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBuyerDeliverAddressBinding.inflate(inflater, this)
    private val sidePadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    private val address = AddressOrderData()

    init {
        binding.tilCityAddress.editText?.doAfterTextChanged(assignWithClearError { address.city = toString() })
        binding.tilStreetAddress.editText?.doAfterTextChanged(assignWithClearError { address.street = toString() })
        binding.tilHouseAddress.editText?.doAfterTextChanged(assignWithClearError { address.house = toString() })
        binding.tilApartmentAddress.editText?.doAfterTextChanged { address.apartment = it.toString() }
        orientation = VERTICAL
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
    }

    private fun assignWithClearError(action: Editable?.() -> Unit) = { text: Editable? ->
        clearError()
        action(text)
    }

    private fun clearError() = with(binding) {
        tilCityAddress.isErrorEnabled = false
        tilHouseAddress.isErrorEnabled = false
        tilStreetAddress.isErrorEnabled = false
    }

    fun validateFields(): Boolean {
        val isValid = binding.tilCityAddress.isAddressLengthValid() && binding.tilStreetAddress.isAddressLengthValid() && binding.tilHouseAddress.isAddressLengthValid()
        if (!isValid) requestFocus()
        return isValid
    }

    fun obtainAddress() = address

    fun setAddress(data: AddressOrderData) = with(data) {
        with(binding) {
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
}