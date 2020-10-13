package com.pharmacy.checkout

import com.pharmacy.checkout.repository.CheckoutLocalDataSource
import com.pharmacy.checkout.repository.CheckoutRemoteDataSource
import com.pharmacy.core.extensions.falseIfNull
import com.pharmacy.data.remote.model.order.CreateOrderRequest
import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData

class CheckoutRepository(
    private val rds: CheckoutRemoteDataSource,
    private val lds: CheckoutLocalDataSource
) {

    val address = lds.addressLiveData

    fun getCustomerInfo() = lds.getCustomerInfo()

    suspend fun sendOrder(body: CreateOrderRequest) = rds.sendOrder(body)

    suspend fun saveAddress(deliveryInfo: DeliveryInfoOrderData) {
        if (deliveryInfo.deliveryType?.isDelivery.falseIfNull() && lds.address == null) {
            lds.saveAddress(deliveryInfo)
        }
    }

}