package com.pharmacy.user.address

import com.pharmacy.core.base.mvvm.BaseViewModel
import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.user.address.repository.AddressRepository

class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    val addressLiveData = repository.address

    fun saveAddress(address: DeliveryInfoOrderData) {
        launchIO {
            repository.saveAddress(address)
        }
    }

}