package com.pulse.components.pharmacy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.core.base.mvvm.BaseViewModel

class PharmacyViewModel(globalProductId: Int, private val useCase: PharmacyUseCase) : BaseViewModel() {

    private var pharmacyList = listOf<Pharmacy>()

    private val _pharmacyLiveData by lazy { MutableLiveData<Int>() }
    val pharmacyLiveData by lazy {
        _pharmacyLiveData.switchMap { pharmacyId ->
            MutableLiveData(pharmacyList.find { it.id == pharmacyId })
        }
    }

    private val _pharmacyListLiveData by lazy { MutableLiveData<Int>(globalProductId) }
    val pharmacyListLiveData by lazy {
        _pharmacyListLiveData.switchMap {
            requestLiveData {
                pharmacyList = useCase.pharmacyList(it).items
                pharmacyList
            }
        }
    }

    private val _addProductItemLiveData by lazy { MutableLiveData<Int>() }
    val addProductLiveData
        get() = _addProductItemLiveData.switchMap { productId ->
            requestLiveData {
                useCase.addProductOrThrow(productId)
            }
        }

    fun getPharmacy(pharmacyId: Int) {
        _pharmacyLiveData.value = pharmacyId
    }

    fun addToCart(globalProductId: Int) {
        _addProductItemLiveData.value = globalProductId
    }
}