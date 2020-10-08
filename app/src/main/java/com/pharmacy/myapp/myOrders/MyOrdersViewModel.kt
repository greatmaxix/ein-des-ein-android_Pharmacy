package com.pharmacy.myapp.myOrders

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.myOrders.repository.MyOrdersPagingSource
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class MyOrdersViewModel : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val stateQueryLiveData by lazy { MutableLiveData(StateQuery.ALL) }

    val myOrdersLiveData by lazy {
        stateQueryLiveData.distinctUntilChanged().switchMap {
            Pager(PagingConfig(ORDER_PAGE_SIZE, initialLoadSize = ORDER_INIT_LOAD_SIZE)) { MyOrdersPagingSource(it.stateQuery) }
                .flow.cachedIn(viewModelScope)
                .asLiveData()
        }
    }

    fun setStateQuery(state: StateQuery) = stateQueryLiveData.postValue(state)

    companion object {
        private const val ORDER_PAGE_SIZE = 10
        private const val ORDER_INIT_LOAD_SIZE = ORDER_PAGE_SIZE * 2
    }

}