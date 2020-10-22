package com.pulse.user.address.repository

import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.user.model.addressAndNote.AddressDAO

class AddressLocalDataSource(private val dao: AddressDAO) {

    suspend fun saveAddress(address: DeliveryInfoOrderData) = dao.insert(address)

    val address
        get() = dao.addressLiveData()

}