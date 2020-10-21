package com.pulse.checkout.model

data class TempPharmacyAddress(
    val imageUrl: String?,
    val availability: String?,
    val name: String?,
    val city: String?,
    val street: String?,
    val houseNo: String?,
    val phone: String?
) {
    companion object {

        fun newMockInstance() = TempPharmacyAddress(
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "3/4 в наличии",
            "Название аптеки",
            "Харьков",
            "ул. Горная",
            "23a",
            "+7 (098) 000 02 00 • +7 (098) 000 02 00"
        )
    }
}