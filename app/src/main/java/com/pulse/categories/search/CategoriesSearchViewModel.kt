package com.pulse.categories.search

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.categories.search.repository.CategoriesSearchPagingSource
import com.pulse.product.BaseProductViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class CategoriesSearchViewModel : BaseProductViewModel() {

    private val searchLiveData by lazy { MutableLiveData<String>() }

    private val _productCountLiveData by lazy { MutableLiveData<Int>() }
    val productCountLiveData: LiveData<Int> by lazy { _productCountLiveData.distinctUntilChanged() }

    val pagedSearchLiveData by lazy {
        searchLiveData.distinctUntilChanged().switchMap {
            Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { CategoriesSearchPagingSource(it, _productCountLiveData::postValue) }
                .flow.cachedIn(viewModelScope)
                .asLiveData()
        }
    }

    fun doCategorySearch(category: String) {
        searchLiveData.value = category
    }
}