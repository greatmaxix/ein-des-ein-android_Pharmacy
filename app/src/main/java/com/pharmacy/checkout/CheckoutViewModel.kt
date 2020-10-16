package com.pharmacy.checkout

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.checkout.CheckoutFragmentDirections.Companion.fromCheckoutToOrder
import com.pharmacy.components.cart.model.CartItem
import com.pharmacy.core.base.mvvm.BaseViewModel
import com.pharmacy.core.general.SingleLiveEvent
import com.pharmacy.core.network.ResponseWrapper
import com.pharmacy.data.remote.model.order.CreateOrderRequest
import com.pharmacy.data.remote.model.order.CustomerOrderData
import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData

class CheckoutViewModel(private val repository: CheckoutRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    val customerInfoLiveData = repository.getCustomerInfo()
    val addressLiveData = repository.address

    fun handlePromoCodeResult(code: String) {
        // todo add on demand
    }

    fun sendOrder(customerInfo: CustomerOrderData, deliveryInfo: DeliveryInfoOrderData, cartItem: CartItem) {
        val orderRequest = CreateOrderRequest(customerInfo, deliveryInfo, cartItem.id, cartItem.productOrderList, cartItem.totalPrice)
        _progressLiveData.value = true
        launchIO {
            repository.saveAddress(deliveryInfo)
            when (val response = repository.sendOrder(orderRequest)) {
                is ResponseWrapper.Success -> _directionLiveData.postValue(fromCheckoutToOrder(response.value.data.item))
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorMessage)
            }
            _progressLiveData.postValue(false)
        }
    }
}