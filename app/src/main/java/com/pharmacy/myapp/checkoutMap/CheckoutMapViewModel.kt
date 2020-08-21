package com.pharmacy.myapp.checkoutMap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.checkoutMap.model.TempAvailableDrugstore
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.data.DummyData

class CheckoutMapViewModel : BaseViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _drugstoresLiveData by lazy { MutableLiveData<ArrayList<TempAvailableDrugstore>>() }
    val drugstoresLiveData: LiveData<ArrayList<TempAvailableDrugstore>> by lazy { _drugstoresLiveData }

    private val _selectedDrugstoreLiveData by lazy { MutableLiveData<TempAvailableDrugstore>() }
    val selectedDrugstoreLiveData: LiveData<TempAvailableDrugstore> by lazy { _selectedDrugstoreLiveData }

    private val _showBottomSheetLiveData by lazy { SingleLiveEvent<TempAvailableDrugstore>() }
    val showBottomSheetLiveData: SingleLiveEvent<TempAvailableDrugstore> by lazy { _showBottomSheetLiveData }

    private val availableDrugstores = DummyData.getAvailableDrugstores()

    fun getDrugstores() = _drugstoresLiveData.postValue(availableDrugstores)

    fun markerClicked(id: Int) = availableDrugstores.find { it.id == id }?.let(_selectedDrugstoreLiveData::postValue)

    fun setSelectedDrugstore(it: TempAvailableDrugstore) {
        _selectedDrugstoreLiveData.postValue(it)
        showBottomSheetLiveData.postValue(it)
    }

    fun setDirection(direction: NavDirections) = _directionLiveData.postValue(direction)

}