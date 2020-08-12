package com.pharmacy.myapp.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import timber.log.Timber

class SearchViewModel : BaseViewModel() {

    private val _oftenLookingLiveData by lazy { MutableLiveData<List<String>>() }
    val oftenLookingLiveData: LiveData<List<String>> by lazy { _oftenLookingLiveData }
    private val _searchLiveData by lazy { MutableLiveData<String>() }
    val searchLiveData: LiveData<String> by lazy { _searchLiveData }

    init {
        _oftenLookingLiveData.postValue(listOf("Спазмы", "Головная боль", "Болит живот", "ОРВИ", "Дротаверин", "Но Шпа", "Терафлю")) // todo
        _searchLiveData.postValue("")
    }

    fun doSearch(value: String) {
        _searchLiveData.postValue(value)
        // repository.doSearch() todo
    }

}