package com.pharmacy.myapp.user.address

import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.data.remote.model.order.AddressOrderData
import com.pharmacy.myapp.user.address.repository.AddressRepository

class AddressViewModel(private val repository: AddressRepository) : BaseViewModel() {

    fun saveAddressAndComment(address: AddressOrderData, comment: String) {
//        repository.saveAddressAndComment(address, comment)
    }

}