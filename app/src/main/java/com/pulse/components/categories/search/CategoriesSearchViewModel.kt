package com.pulse.components.categories.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pulse.components.categories.search.repository.CategoriesSearchPagingSource
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.product.model.ProductLite
import com.pulse.core.utils.flow.StateEventFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.component.KoinApiExtension

@ExperimentalCoroutinesApi
@KoinApiExtension
class CategoriesSearchViewModel : BaseProductViewModel() {

    private val searchState = StateEventFlow("")
    val productCountState = StateEventFlow(0)

    val pagedSearchState: Flow<PagingData<ProductLite>> = searchState.flatMapLatest {
        Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { CategoriesSearchPagingSource(it, productCountState::postState) }
            .flow
            .cachedIn(viewModelScope)
    }

    fun doCategorySearch(category: String) = searchState.postState(category)
}