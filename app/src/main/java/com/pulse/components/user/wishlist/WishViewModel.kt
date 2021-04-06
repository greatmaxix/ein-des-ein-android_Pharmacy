package com.pulse.components.user.wishlist

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.user.wishlist.repository.WishPagingSource
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class WishViewModel : BaseProductViewModel() {

    val wishFlow by lazy {
        Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { WishPagingSource() }
            .flow
            .cachedIn(viewModelScope)
    }
}