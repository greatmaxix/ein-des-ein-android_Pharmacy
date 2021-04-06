package com.pulse.components.orders

import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.fromOrderToOrderDetails
import com.pulse.R
import com.pulse.components.orders.StateQuery.*
import com.pulse.components.orders.adapter.OrdersAdapter
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.isEmpty
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentOrdersBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension

@ExperimentalCoroutinesApi
@KoinApiExtension
class OrdersFragment : BaseToolbarFragment<OrdersViewModel>(R.layout.fragment_orders, OrdersViewModel::class) {

    private val ordersAdapter by lazy { OrdersAdapter(viewModel::getOrderDetail) }
    private val binding by viewBinding(FragmentOrdersBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        initRecyclerView()

        cgState.setOnCheckedChangeListener { _, checkedId ->
            val state = when (checkedId) {
                R.id.chip_in_process -> IN_PROGRESS
                R.id.chip_canceled -> CANCELED
                R.id.chip_done -> DONE
                else -> ALL
            }
            viewModel.setStateQuery(state)
        }
        ordersAdapter.addLoadStateListener(::showProgress)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateQuery(ALL)
    }

    override fun onPause() {
        super.onPause()
        uiHelper.showLoading(false)
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.ordersLiveData) { ordersAdapter.submitData(lifecycle, it) }
        observe(viewModel.orderState) { it?.let { navController.navigate(fromOrderToOrderDetails(it)) } }
    }

    private fun showProgress(it: CombinedLoadStates) {
        val isLoadingFinished = it.prepend is LoadState.NotLoading && it.prepend.endOfPaginationReached
        if (isLoadingFinished) {
            binding.llOrdersNotFoundContainer.visibleOrGone(ordersAdapter.isEmpty())
            binding.rvOrders.visibleOrGone(ordersAdapter.isEmpty().not())
        }
        val isNeedSHowProgress = it.refresh is LoadState.Loading || it.append is LoadState.Loading
        uiHelper.showLoading(isNeedSHowProgress)
    }

    private fun initRecyclerView() = binding.rvOrders.apply {
        addItemDecorator()
        adapter = ordersAdapter
    }
}