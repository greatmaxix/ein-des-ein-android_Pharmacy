package com.pulse.orders

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import com.pulse.core.network.ResponseWrapper.Error
import com.pulse.core.network.ResponseWrapper.Success
import com.pulse.model.order.Order
import com.pulse.orders.repository.OrdersPagingSource
import com.pulse.orders.repository.OrdersRepository
import org.koin.core.component.KoinApiExtension

@KoinApiExtension

class OrdersViewModel(private val repository: OrdersRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _orderLiveData by lazy { SingleLiveEvent<Order>() }
    val orderLiveData: LiveData<Order> by lazy { _orderLiveData }

    private val stateQueryLiveData by lazy { MutableLiveData(StateQuery.ALL) }

    val ordersLiveData by lazy {
        stateQueryLiveData.distinctUntilChanged().switchMap {
            Pager(PagingConfig(ORDER_PAGE_SIZE, initialLoadSize = ORDER_INIT_LOAD_SIZE)) { OrdersPagingSource(it.stateQuery) }
                .flow.cachedIn(viewModelScope)
                .asLiveData()
        }
    }

    fun setStateQuery(state: StateQuery) = stateQueryLiveData.postValue(state)

    fun getOrderDetail(id: Int) {
        _progressLiveData.value = true
        launchIO {
            when (val response = repository.getOrderDetail(id)) {
                is Success -> _orderLiveData.postValue(response.value.data.item)
                is Error -> _errorLiveData.postValue(response.errorMessage)
            }
            _progressLiveData.postValue(false)
        }
    }

    companion object {
        private const val ORDER_PAGE_SIZE = 10
        private const val ORDER_INIT_LOAD_SIZE = ORDER_PAGE_SIZE * 2
    }

}
