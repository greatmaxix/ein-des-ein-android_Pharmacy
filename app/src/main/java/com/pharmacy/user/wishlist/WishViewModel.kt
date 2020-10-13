package com.pharmacy.user.wishlist

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.product.BaseProductViewModel
import com.pharmacy.user.wishlist.repository.WishPagingSource
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class WishViewModel : BaseProductViewModel() {

    val wishLiveData by lazy {
        Pager(PagingConfig(PAGE_SIZE, initialLoadSize = INIT_LOAD_SIZE)) { WishPagingSource() }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

}