package com.pharmacy.myapp.pharmacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.data.DummyData
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import com.pharmacy.myapp.pharmacy.repository.PharmacyPagingSource
import com.pharmacy.myapp.pharmacy.repository.PharmacyRepository
import com.pharmacy.myapp.product.BaseProductViewModel

class PharmacyViewModel(private val globalProductId: Int, private val repository: PharmacyRepository) : BaseViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _drugstoresLiveData by lazy { MutableLiveData<ArrayList<Pharmacy>>() }
    val drugstoresLiveData: LiveData<ArrayList<Pharmacy>> by lazy { _drugstoresLiveData }

    private val _selectedDrugstoreLiveData by lazy { MutableLiveData<Pharmacy>() }
    val selectedDrugstoreLiveData: LiveData<Pharmacy> by lazy { _selectedDrugstoreLiveData }

    private val _showBottomSheetLiveData by lazy { SingleLiveEvent<Pharmacy>() }
    val showBottomSheetLiveData: SingleLiveEvent<Pharmacy> by lazy { _showBottomSheetLiveData }

    private val availableDrugstores = DummyData.getAvailableDrugstores()

    fun getDrugstores() = _drugstoresLiveData.postValue(availableDrugstores)

    fun markerClicked(id: Int) = availableDrugstores.find { it.id == id }?.let(_selectedDrugstoreLiveData::postValue)

    fun setSelectedDrugstore(it: Pharmacy) {
        _selectedDrugstoreLiveData.postValue(it)
        showBottomSheetLiveData.postValue(it)
    }

    fun setDirection(direction: NavDirections) = _directionLiveData.postValue(direction)

    val pharmacyLiveData by lazy {
        Pager(PagingConfig(BaseProductViewModel.PAGE_SIZE, initialLoadSize = BaseProductViewModel.INIT_LOAD_SIZE)) { PharmacyPagingSource(globalProductId) }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }
}