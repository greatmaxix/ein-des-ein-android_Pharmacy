package com.pulse.components.orders.details

import androidx.lifecycle.viewModelScope
import com.pulse.components.orders.details.repository.OrderDetailsRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent

class OrderDetailsViewModel(private val repository: OrderDetailsRepository) : BaseViewModel() {

    val isCancelSuccessEvent = SingleShotEvent<Boolean>()

    fun cancelOrder(id: Int) = viewModelScope.execute {
        repository.cancelOrder(id)
        isCancelSuccessEvent.offerEvent(true)
    }
}