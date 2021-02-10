package com.pulse.components.orders

import android.os.Bundle
import android.view.View
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.fromOrderToOrderDetails
import com.pulse.R
import com.pulse.components.orders.StateQuery.*
import com.pulse.components.orders.adapter.OrdersAdapter
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.isEmpty
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.FragmentOrdersBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OrdersFragment(private val viewModel: OrdersViewModel) : BaseMVVMFragment(R.layout.fragment_orders) {

    private val ordersAdapter by lazy { OrdersAdapter(viewModel::getOrderDetail) }
    private val binding by viewBinding(FragmentOrdersBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

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
        progressCallback?.setInProgress(false)
    }

    override fun onBindLiveData() = with(viewModel) {
        observe(errorLiveData) { messageCallback?.showError(it) }
        observe(progressLiveData) { progressCallback?.setInProgress(it) }

        observe(directionLiveData, navController::navigate)
        observe(ordersLiveData) { ordersAdapter.submitData(lifecycle, it) }
        observe(orderLiveData) { doNav(fromOrderToOrderDetails(it)) }
    }

    private fun showProgress(it: CombinedLoadStates) {
        val isLoadingFinished = it.prepend is LoadState.NotLoading && it.prepend.endOfPaginationReached
        if (isLoadingFinished) {
            binding.llOrdersNotFoundContainer.visibleOrGone(ordersAdapter.isEmpty())
            binding.rvOrders.visibleOrGone(ordersAdapter.isEmpty().not())
        }
        val isNeedSHowProgress = it.refresh is LoadState.Loading || it.append is LoadState.Loading
        progressCallback?.setInProgress(isNeedSHowProgress)
    }

    private fun initRecyclerView() = binding.rvOrders.apply {
        addItemDecorator()
        adapter = ordersAdapter
    }
}