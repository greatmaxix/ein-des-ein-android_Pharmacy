package com.pulse.checkout.repository

import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.user.model.addressAndNote.AddressDAO
import com.pulse.user.model.customer.CustomerDAO

class CheckoutLocalDataSource(private val dao: CustomerDAO, private val addressDAO: AddressDAO) {

    val addressLiveData
        get() = addressDAO.addressLiveData()

    val address
        get() = addressDAO.get()

    fun getCustomerInfo() = dao.customerLiveData()

    suspend fun saveAddress(deliveryInfo: DeliveryInfoOrderData) = addressDAO.insert(deliveryInfo)
}