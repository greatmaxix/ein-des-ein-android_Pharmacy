package com.pharmacy.myapp.pharmacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import com.pharmacy.myapp.pharmacy.repository.PharmacyRepository

class PharmacyViewModel(globalProductId: Int, private val repository: PharmacyRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<Int>() }
    val errorLiveData: LiveData<Int> by lazy { _errorLiveData }

    private val _pharmacyListLiveData by lazy { MutableLiveData<MutableList<Pharmacy>>() }
    val pharmacyListLiveData: LiveData<MutableList<Pharmacy>> by lazy { _pharmacyListLiveData }

    private val _pharmacyLiveData by lazy { MutableLiveData<Pharmacy>() }
    val pharmacyLiveData: LiveData<Pharmacy> by lazy { _pharmacyLiveData }

    private val _cartNotifyLiveData by lazy { SingleLiveEvent<Unit>() }
    val cartNotifyLiveData: LiveData<Unit> by lazy { _cartNotifyLiveData }

    private var pharmacyList = mutableListOf<Pharmacy>()
        set(value) {
            field = value
            _pharmacyListLiveData.postValue(field)
        }

    init {
        getPharmacyList(globalProductId)
    }

    private fun getPharmacyList(globalProductId: Int) {
        _progressLiveData.value = true
        launchIO {
            when (val response = repository.pharmacyList(globalProductId)) {
                is ResponseWrapper.Success -> pharmacyList = response.value.data.items.toMutableList()
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    fun getPharmacy(pharmacyId: Int) {
        pharmacyList.find { it.id == pharmacyId }?.let { _pharmacyLiveData.value = it }
    }

    fun addToCart(globalProductId: Int) {
        _progressLiveData.value = true
        launchIO {
            when (val response = repository.addToCart(globalProductId)) {
                is ResponseWrapper.Success -> _cartNotifyLiveData.postValue(Unit)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }
}