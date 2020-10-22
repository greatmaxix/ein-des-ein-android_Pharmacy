package com.pulse.user.address.repository

import com.pulse.data.remote.model.order.DeliveryInfoOrderData

class AddressRepository(private val lds: AddressLocalDataSource) {

    val address
        get() = lds.address

    suspend fun saveAddress(address: DeliveryInfoOrderData) = lds.saveAddress(address)

}