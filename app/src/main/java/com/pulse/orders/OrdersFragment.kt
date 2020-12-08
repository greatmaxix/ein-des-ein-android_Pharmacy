package com.pulse.orders

import android.os.Bundle
import android.view.View
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.pulse.MainGraphDirections.Companion.fromOrderToOrderDetails
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.isEmpty
import com.pulse.core.extensions.visibleOrGone
import com.pulse.orders.StateQuery.*
import com.pulse.orders.adapter.OrdersAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OrdersFragment(private val viewModel: OrdersViewModel) : BaseMVVMFragment(R.layout.fragment_orders) {

    private val ordersAdapter by lazy { OrdersAdapter(viewModel::getOrderDetail) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        initRecyclerView()
        cgState.setOnCheckedChangeListener { _, checkedId ->
            val state = when (checkedId) {
                R.id.chipMyInProcess -> IN_PROGRESS
                R.id.chipMyCanceled -> CANCELED
                R.id.chipMyDone -> DONE
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
            llOrdersNotFoundContainer.visibleOrGone(ordersAdapter.isEmpty())
            rvOrdersList.visibleOrGone(ordersAdapter.isEmpty().not())
        }
        val isNeedSHowProgress = it.refresh is LoadState.Loading || it.append is LoadState.Loading
        progressCallback?.setInProgress(isNeedSHowProgress)
    }

    private fun initRecyclerView() = rvOrdersList.apply {
        addItemDecorator()
        adapter = ordersAdapter
    }

}