package com.pulse.components.user.address

import com.pulse.components.user.address.repository.AddressRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.data.remote.model.order.DeliveryInfoOrderData

class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    val addressLiveData = repository.address

    fun saveAddress(address: DeliveryInfoOrderData) {
        launchIO {
            repository.saveAddress(address)
        }
    }

}