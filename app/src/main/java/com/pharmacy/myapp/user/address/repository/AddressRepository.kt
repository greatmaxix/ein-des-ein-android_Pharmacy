package com.pharmacy.myapp.user.address.repository

import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData

class AddressRepository(private val lds: AddressLocalDataSource) {

    val address
        get() = lds.address

    suspend fun saveAddress(address: DeliveryInfoOrderData) = lds.saveAddress(address)

}