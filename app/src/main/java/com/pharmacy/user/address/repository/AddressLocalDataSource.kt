package com.pharmacy.user.address.repository

import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.user.model.addressAndNote.AddressDAO

class AddressLocalDataSource(private val dao: AddressDAO) {

    suspend fun saveAddress(address: DeliveryInfoOrderData) = dao.insert(address)

    val address
        get() = dao.addressLiveData()

}