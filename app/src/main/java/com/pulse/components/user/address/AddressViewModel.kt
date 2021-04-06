package com.pulse.components.user.address

import androidx.lifecycle.viewModelScope
import com.pulse.components.user.address.repository.AddressRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.data.remote.model.order.DeliveryInfoOrderData

class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    val addressFlow = repository.address

    fun saveAddress(address: DeliveryInfoOrderData) = viewModelScope.execute {
        repository.saveAddress(address)
    }
}