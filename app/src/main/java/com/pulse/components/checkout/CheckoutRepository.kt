package com.pulse.components.checkout

import com.pulse.components.checkout.repository.CheckoutLocalDataSource
import com.pulse.components.checkout.repository.CheckoutRemoteDataSource
import com.pulse.core.extensions.falseIfNull
import com.pulse.data.remote.model.order.CreateOrderRequest
import com.pulse.data.remote.model.order.DeliveryInfoOrderData

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