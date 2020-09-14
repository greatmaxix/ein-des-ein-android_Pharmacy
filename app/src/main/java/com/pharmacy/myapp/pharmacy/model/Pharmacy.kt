package com.pharmacy.myapp.pharmacy.model

data class Pharmacy(
    val id: Int,
    val phone: String,
    val name: String,
    val location: PharmacyLocation,
    val logo: PharmacyLogo,
    val pharmacyProducts: List<PharmacyProducts>
)