package com.pulse.components.orders

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pulse.components.orders.repository.OrdersPagingSource
import com.pulse.components.orders.repository.OrdersRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.model.order.Order
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalCoroutinesApi
class OrdersViewModel(private val repository: OrdersRepository) : BaseViewModel() {

    val orderState = StateEventFlow<Order?>(null)
    private val stateQueryState = StateEventFlow<StateQuery?>(StateQuery.ALL)

    val ordersLiveData by lazy {
        stateQueryState.flatMapLatest {
            Pager(PagingConfig(ORDER_PAGE_SIZE, initialLoadSize = ORDER_INIT_LOAD_SIZE)) { OrdersPagingSource(it?.stateQuery ?: StateQuery.ALL.stateQuery) }
                .flow
                .cachedIn(viewModelScope)
        }
    }

    fun setStateQuery(state: StateQuery) = stateQueryState.postState(state)

    fun getOrderDetail(id: Int) = viewModelScope.execute {
        orderState.postState(repository.getOrderDetail(id).data.item)
    }

    companion object {

        private const val ORDER_PAGE_SIZE = 10
        private const val ORDER_INIT_LOAD_SIZE = ORDER_PAGE_SIZE * 2
    }
}
