package com.pulse.components.pharmacy

import androidx.lifecycle.viewModelScope
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.components.pharmacy.repository.PharmacyUseCase
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.data.GeneralException

class PharmacyViewModel(globalProductId: Int, private val useCase: PharmacyUseCase) : BaseViewModel() {

    private var pharmacyList = listOf<Pharmacy>()

    val pharmacyEvent = SingleShotEvent<Pharmacy?>()
    val errorEvent = SingleShotEvent<GeneralException>()
    val pharmacyListState = StateEventFlow<List<Pharmacy>>(listOf())
    val addProductResultEvent = SingleShotEvent<Unit>()

    init {
        viewModelScope.execute {
            pharmacyList = useCase.pharmacyList(globalProductId).items
            pharmacyListState.postState(pharmacyList)
        }
    }

    fun getPharmacy(pharmacyId: Int) = pharmacyEvent.offerEvent(pharmacyList.find { it.id == pharmacyId })

    fun addToCart(globalProductId: Int) = viewModelScope.execute {
        try {
            useCase.addProductOrThrow(globalProductId)
            addProductResultEvent.offerEvent(Unit)
        } catch (e: GeneralException) {
            errorEvent.offerEvent(e)
        }
    }
}