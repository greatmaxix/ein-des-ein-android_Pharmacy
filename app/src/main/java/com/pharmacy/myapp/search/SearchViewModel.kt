package com.pharmacy.myapp.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.search.repository.SearchDataSource
import com.pharmacy.myapp.search.repository.SearchRepository

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val searchLiveData by lazy { MutableLiveData<String>() }

    private val _productCountLiveData by lazy { MutableLiveData<Int>() }
    val productCountLiveData: LiveData<Int> by lazy { _productCountLiveData.distinctUntilChanged() }

    val pagedSearchLiveData = searchLiveData.switchMap {
        Pager(PagingConfig(20, initialLoadSize = 40)) { SearchDataSource(it) { _productCountLiveData.postValue(it) } }.flow.cachedIn(viewModelScope).asLiveData()
    }

    fun doSearch(value: String) = searchLiveData.postValue(value)

    fun getProductInfo() {

    }
}
