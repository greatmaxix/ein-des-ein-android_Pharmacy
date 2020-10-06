package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.checkout.repository.CheckoutLocalDataSource
import com.pharmacy.myapp.checkout.repository.CheckoutRemoteDataSource
import com.pharmacy.myapp.core.extensions.falseIfNull
import com.pharmacy.myapp.data.remote.model.order.CreateOrderRequest
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData

class CheckoutRepository(
    private val crds: CheckoutRemoteDataSource,
    private val clds: CheckoutLocalDataSource
) {

    val address = clds.addressLiveData

    fun getCustomerInfo() = clds.getCustomerInfo()

    suspend fun sendOrder(body: CreateOrderRequest) = crds.sendOrder(body)

    suspend fun saveAddress(deliveryInfo: DeliveryInfoOrderData) {
        if (deliveryInfo.deliveryType?.isDelivery.falseIfNull() && clds.address == null) {
            clds.saveAddress(deliveryInfo)
        }
    }

}