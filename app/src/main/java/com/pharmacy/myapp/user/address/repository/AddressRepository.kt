package com.pharmacy.myapp.user.address.repository

import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData

class AddressRepository(private val alds: AddressLocalDataSource) {

    val address
        get() = alds.address

    suspend fun saveAddress(address: DeliveryInfoOrderData) = alds.saveAddress(address)

}