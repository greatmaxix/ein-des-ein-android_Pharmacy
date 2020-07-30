package com.pharmacy.myapp.checkout.model

data class TempDeliveryAddress(
    var city: String?,
    var street: String?,
    var houseNo: String?,
    var apartmentNo: String?,
    var timing: String?
) {

    fun isEmpty() = city.isNullOrBlank() || street.isNullOrBlank() || houseNo.isNullOrBlank() || apartmentNo.isNullOrBlank()

    companion object {

        fun empty() = TempDeliveryAddress(
            "",
            "",
            "",
            "",
            ""
        )
    }
}