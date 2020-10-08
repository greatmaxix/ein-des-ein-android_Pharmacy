package com.pharmacy.myapp.user.address

import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.myapp.user.address.repository.AddressRepository

class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    val addressLiveData = repository.address

    fun saveAddress(address: DeliveryInfoOrderData) {
        launchIO {
            repository.saveAddress(address)
        }
    }

}