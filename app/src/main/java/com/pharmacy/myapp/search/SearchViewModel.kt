package com.pharmacy.myapp.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.R
import com.pharmacy.myapp.produtcList.BaseProductListViewModel
import com.pharmacy.myapp.search.repository.SearchPagingSource
import com.pharmacy.myapp.search.repository.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : BaseProductListViewModel() {

    private val searchLiveData by lazy { MutableLiveData("") }

    private val _productCountLiveData by lazy { MutableLiveData<Int>() }
    val productCountLiveData: LiveData<Int> by lazy { _productCountLiveData.distinctUntilChanged() }

    val pagedSearchLiveData by lazy {
        searchLiveData.distinctUntilChanged().switchMap {
            Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { SearchPagingSource(it, _productCountLiveData::postValue) }
                .flow.cachedIn(viewModelScope)
                .asLiveData()
        }
    }

    fun doSearch(value: String) = searchLiveData.postValue(value)

    override fun setOrRemoveWish(setOrRemove: Triple<Boolean, Int, Int>) {
        launchIO {
            if (searchRepository.isCustomerExist()) {
                super.setOrRemoveWish(setOrRemove)
            } else {
                _errorLiveData.postValue(R.string.forAddingProduct)
            }
        }
    }
}