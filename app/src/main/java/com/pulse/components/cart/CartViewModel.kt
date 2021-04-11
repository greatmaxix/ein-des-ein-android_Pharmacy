package com.pulse.components.cart

import androidx.lifecycle.viewModelScope
import com.pulse.components.cart.model.CartItem
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.data.GeneralException

class CartViewModel(private val useCase: CartUseCase) : BaseViewModel() {

    val cartItemState = StateEventFlow<List<CartItem>>(listOf())
    val errorEvent = SingleShotEvent<GeneralException>()
    val removeItemEvent = SingleShotEvent<Int>()

    fun removeProductFromCart(productId: Int) {
        viewModelScope.execute {
            useCase.removeProduct(productId)
            removeItemEvent.offerEvent(productId)
        }
    }

    fun updateProducts() {
        viewModelScope.execute {
            try {
                cartItemState.postState(useCase.getProducts())
            } catch (e: GeneralException) {
                errorEvent.offerEvent(e)
            }
        }
    }
}