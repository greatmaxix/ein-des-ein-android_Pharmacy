package com.pulse.orders

import android.os.Bundle
import android.view.View
import com.pulse.MainGraphDirections.Companion.fromOrderToOrderDetails
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.addItemDecorator
import com.pulse.core.extensions.addStateListener
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
        ordersAdapter.addStateListener { progressCallback?.setInProgress(it) }
    }

    private fun initRecyclerView() = rvOrdersList.apply {
        addItemDecorator()
        adapter = ordersAdapter
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.ordersLiveData) { ordersAdapter.submitData(lifecycle, it) }
        observe(viewModel.orderLiveData) { doNav(fromOrderToOrderDetails(it)) }
    }

}