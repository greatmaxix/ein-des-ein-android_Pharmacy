package com.pharmacy.myapp.user.address.repository

import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.myapp.user.model.addressAndNote.AddressDAO

class AddressLocalDataSource(private val dao: AddressDAO) {

    suspend fun saveAddress(address: DeliveryInfoOrderData) = dao.insert(address)

    val address
        get() = dao.addressLiveData()

}