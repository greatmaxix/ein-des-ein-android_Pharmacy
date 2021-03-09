package com.pulse.components.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.search.repository.SearchPagingSource
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class SearchViewModel : BaseProductViewModel() {

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

    fun doSearch(value: String) {
        searchLiveData.value = value
    }
}