package com.pulse.components.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.search.repository.SearchPagingSource
import com.pulse.core.utils.flow.StateEventFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalCoroutinesApi
class SearchViewModel : BaseProductViewModel() {

    private val searchState = StateEventFlow("")
    val productCountState = StateEventFlow(0)
    val pagedSearchFlow = searchState.flatMapLatest {
        Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { SearchPagingSource(it, productCountState::postState) }
            .flow
            .cachedIn(viewModelScope)
    }

    fun doSearch(value: String) = searchState.postState(value)
}