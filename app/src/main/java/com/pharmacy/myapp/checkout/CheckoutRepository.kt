package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.checkout.repository.CheckoutLocalDataSource
import com.pharmacy.myapp.checkout.repository.CheckoutRemoteDataSource
import com.pharmacy.myapp.core.extensions.falseIfNull
import com.pharmacy.myapp.data.remote.model.order.CreateOrderRequest
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData

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