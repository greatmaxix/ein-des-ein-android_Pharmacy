package com.pulse.components.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.pulse.core.base.mvvm.BaseViewModel

class CartViewModel(private val useCase: CartUseCase) : BaseViewModel() {

    val cartItemLiveData
        get() = requestLiveData {
            useCase.getProducts()
        }

    private val _removeItemLiveData by lazy { MutableLiveData<Int>() }

    val removeItemLiveData by lazy {
        _removeItemLiveData.switchMap { productId ->
            requestLiveData {
                useCase.removeProduct(productId)
                productId
            }
        }
    }

    fun removeProductFromCart(productId: Int) {
        _removeItemLiveData.value = productId
    }
}