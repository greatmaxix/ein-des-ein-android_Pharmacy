package com.pharmacy.myapp.checkoutMap.model

data class TempAvailableDrugstore(
    var availability: String,
    val name: String,
    val address: String,
    val contactInfo: String,
    val workingHours: String,
    val price: String
)