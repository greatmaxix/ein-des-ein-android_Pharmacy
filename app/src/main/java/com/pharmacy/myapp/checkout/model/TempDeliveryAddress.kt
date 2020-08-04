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

        fun newInstance() = TempDeliveryAddress(
            "",
            "",
            "",
            "",
            ""
        )

        fun newMockInstance() = TempDeliveryAddress(
            "Харьков",
            "ул. Горная",
            "23a",
            "кв. 56",
            "c 8:00 до 22:00 ежедневно"
        )
    }
}