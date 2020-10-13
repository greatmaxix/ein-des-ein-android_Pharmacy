package com.pharmacy.pharmacy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PharmacyProducts(val pharmacyProductId: Int, val price: Float) : Parcelable