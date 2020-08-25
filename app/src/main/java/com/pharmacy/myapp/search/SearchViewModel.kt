package com.pharmacy.myapp.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.search.repository.SearchDataSource
import com.pharmacy.myapp.search.repository.SearchRepository

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val searchLiveData by lazy { MutableLiveData("") }

    private val _productCountLiveData by lazy { MutableLiveData<Int>() }
    val productCountLiveData: LiveData<Int> by lazy { _productCountLiveData.distinctUntilChanged() }

    private val _productLiveData by lazy { SingleLiveEvent<Product>() }
    val productLiveData: LiveData<Product> by lazy { _productLiveData }

    val pagedSearchLiveData = searchLiveData.switchMap {
        Pager(PagingConfig(20, initialLoadSize = 40)) { SearchDataSource(it) { _productCountLiveData.postValue(it) } }.flow.cachedIn(viewModelScope).asLiveData()
    }

    fun doSearch(value: String) = searchLiveData.postValue(value)

    fun getProductInfo(globalProductId: Int) {

        launchIO {
            _progressLiveData.postValue(true)
            val response = repository.productById(globalProductId)
            _progressLiveData.postValue(false)
            when (response) {
                is ResponseWrapper.Success -> _productLiveData.postValue(response.value.data.item)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorMessage)
            }
        }

    }
}
