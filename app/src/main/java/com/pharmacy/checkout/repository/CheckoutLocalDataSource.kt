package com.pharmacy.checkout.repository

import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.user.model.addressAndNote.AddressDAO
import com.pharmacy.user.model.customer.CustomerDAO

class CheckoutLocalDataSource(private val dao: CustomerDAO, private val addressDAO: AddressDAO) {

    val addressLiveData
        get() = addressDAO.addressLiveData()

    val address
        get() = addressDAO.get()

    fun getCustomerInfo() = dao.customerLiveData()

    suspend fun saveAddress(deliveryInfo: DeliveryInfoOrderData) = addressDAO.insert(deliveryInfo)
}