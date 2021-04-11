package com.pulse.components.checkout

import androidx.lifecycle.viewModelScope
import com.pulse.components.cart.model.CartItem
import com.pulse.components.checkout.CheckoutFragmentDirections.Companion.fromCheckoutToOrder
import com.pulse.components.checkout.repository.CheckoutRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.data.remote.model.order.CreateOrderRequest
import com.pulse.data.remote.model.order.CustomerOrderData
import com.pulse.data.remote.model.order.DeliveryInfoOrderData

class CheckoutViewModel(private val repository: CheckoutRepository) : BaseViewModel() {

    val customerInfoState = repository.getCustomerInfo()
    val addressState = repository.address

    fun handlePromoCodeResult(code: String) {
        // todo add on demand
    }

    fun sendOrder(customerInfo: CustomerOrderData, deliveryInfo: DeliveryInfoOrderData, cartItem: CartItem) = viewModelScope.execute {
        val orderRequest = CreateOrderRequest(customerInfo, deliveryInfo, cartItem.id, cartItem.productOrderList, 0.0)
        repository.saveAddress(deliveryInfo)
        val response = repository.sendOrder(orderRequest)
        navEvent.postEvent(fromCheckoutToOrder(response.data.item))
    }
}