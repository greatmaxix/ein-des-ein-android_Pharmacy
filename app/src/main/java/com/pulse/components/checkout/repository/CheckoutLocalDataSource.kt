package com.pulse.components.checkout.repository

import com.pulse.components.user.model.addressAndNote.AddressDAO
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.data.remote.model.order.DeliveryInfoOrderData

class CheckoutLocalDataSource(private val dao: CustomerDAO, private val addressDAO: AddressDAO) {

    val addressLiveData
        get() = addressDAO.addressLiveData()

    val address
        get() = addressDAO.get()

    fun getCustomerInfo() = dao.customerLiveData()

    suspend fun saveAddress(deliveryInfo: DeliveryInfoOrderData) = addressDAO.insert(deliveryInfo)
}