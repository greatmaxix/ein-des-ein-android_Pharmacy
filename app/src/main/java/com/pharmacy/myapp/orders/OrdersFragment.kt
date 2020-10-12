package com.pharmacy.myapp.orders

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.MainGraphDirections.Companion.fromOrderToOrderDetails
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.core.extensions.addStateListener
import com.pharmacy.myapp.orders.StateQuery.*
import com.pharmacy.myapp.orders.adapter.OrdersAdapter
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