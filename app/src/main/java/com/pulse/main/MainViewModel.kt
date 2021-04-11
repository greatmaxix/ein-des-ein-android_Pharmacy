package com.pulse.main

import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.R
import com.pulse.components.user.model.customer.Customer
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import kotlinx.coroutines.flow.onEach

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {

    val directionState = StateEventFlow(0)
    private var customer: Customer? = null
    val customerInfoFlow = repository.customerFlow().onEach {
        customer = it
    }

    fun navSelected(@IdRes id: Int) = viewModelScope.execute {
        val isCustomerExist = repository.isCustomerExist()
        directionState.postState(if (id == R.id.profile_graph && !isCustomerExist) R.id.profile_graph_guest else id)
    }

    fun setChatForeground(isForeground: Boolean) = viewModelScope.execute { repository.setChatForeground(isForeground) }

    fun goToChat(chatId: Int) = viewModelScope.execute {
        val response = repository.getChat(chatId)
        navEvent.postEvent(globalToChat(response))
    }

    fun getCurrentCustomer() = customer
}