package com.pharmacy.myapp.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.model.region.Region
import com.pharmacy.myapp.model.region.RegionWithHeader
import com.pharmacy.myapp.region.repository.RegionRepository
import kotlinx.coroutines.delay

class RegionViewModel(private val repository: RegionRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _regionsLiveData by lazy { SingleLiveEvent<MutableList<RegionWithHeader>>() }
    val regionsLiveData: LiveData<MutableList<RegionWithHeader>> by lazy { _regionsLiveData }

    private val _regionSavedLiveData by lazy { MutableLiveData<Boolean>() }
    val regionSavedLiveData: LiveData<Boolean> by lazy { _regionSavedLiveData }

    private var originalList = emptyList<Region>()
    private var itemClicked = false

    init {
        _progressLiveData.value = true
        launchIO {
            when (val response = repository.getRegions()) {
                is Success -> {
                    val items = response.value.data.items
                    originalList = items
                    _regionsLiveData.postValue(addHeaders(items.toMutableList()))
                    _progressLiveData.postValue(false)
                }
                is Error -> {
                    _errorLiveData.postValue(response.errorMessage)
                    _progressLiveData.postValue(false)
                }
            }
        }
    }

    private fun addHeaders(list: MutableList<Region>): MutableList<RegionWithHeader> {
        var omittedList: MutableList<Region> = list
        if (list.size == 1) {
            omittedList = list[0].children.toMutableList()
        }
        val withHeader = mutableListOf<RegionWithHeader>()
        omittedList.forEachIndexed { index, region ->
            if (index == 0 || region.firstCharOfName != omittedList[index - 1].firstCharOfName) {
                withHeader.add(RegionWithHeader(null, region.name.first()))
            }
            withHeader.add(RegionWithHeader(region, 0.toChar()))
        }
        return withHeader
    }

    fun regionSelected(region: Region) {
        val children = region.children
        if (children.isNullOrEmpty()) {
            setRegion(region)
        } else {
            _regionsLiveData.postValue(addHeaders(children.toMutableList()))
            itemClicked = true
        }
    }

    private fun setRegion(region: Region) {
        launchIO {
            if (repository.getCustomer() == null) {
                repository.saveRegionLocally(region)
            } else {
                when (val response = repository.updateCustomerRegion(region.id)) {
                    is Success -> repository.saveCustomerInfoLocally(response.value.data.item)
                    is Error -> _errorLiveData.postValue(response.errorMessage)
                }
            }
            delay(1000) // wait for animation end
            _regionSavedLiveData.postValue(true)
        }
    }

    fun handleBackPress() {
        if (itemClicked) {
            itemClicked = false
            _regionsLiveData.postValue(addHeaders(originalList.toMutableList()))
            return
        }
        _regionSavedLiveData.postValue(false)
    }

}