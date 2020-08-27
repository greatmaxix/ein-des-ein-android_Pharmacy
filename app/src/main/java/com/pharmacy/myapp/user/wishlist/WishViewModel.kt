package com.pharmacy.myapp.user.wishlist

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.produtcList.BaseProductListViewModel
import com.pharmacy.myapp.user.wishlist.repository.WishPagingSource

class WishViewModel : BaseProductListViewModel() {



    val wishLiveData = Pager(PagingConfig(20, initialLoadSize = 40)) { WishPagingSource() }.flow
        .cachedIn(viewModelScope)
        .asLiveData()
}