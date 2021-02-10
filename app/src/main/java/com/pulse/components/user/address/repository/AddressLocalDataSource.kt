package com.pulse.components.user.address.repository

import com.pulse.components.user.model.addressAndNote.AddressDAO
import com.pulse.data.remote.model.order.DeliveryInfoOrderData

class AddressLocalDataSource(private val dao: AddressDAO) {

    suspend fun saveAddress(address: DeliveryInfoOrderData) = dao.insert(address)

    val address
        get() = dao.addressLiveData()

}