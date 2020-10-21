package com.pulse.user.address

import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.user.address.repository.AddressRepository

class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    val addressLiveData = repository.address

    fun saveAddress(address: DeliveryInfoOrderData) {
        launchIO {
            repository.saveAddress(address)
        }
    }

}