package com.pharmacy.myapp.myOrders

import android.os.Bundle
import android.view.View
import androidx.paging.LoadState
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.myOrders.StateQuery.*
import com.pharmacy.myapp.myOrders.adapter.MyOrdersAdapter
import kotlinx.android.synthetic.main.fragment_my_orders.*

class MyOrdersFragment(private val viewModel: MyOrdersViewModel) : BaseMVVMFragment(R.layout.fragment_my_orders) {

    private val adapter by lazy { MyOrdersAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        rvOrdersListMyOrders.addItemDecorator()
        rvOrdersListMyOrders.adapter = adapter
        cgState.setOnCheckedChangeListener { _, checkedId ->
            val state = when (checkedId) {
                R.id.chipMyInProcess -> IN_PROGRESS
                R.id.chipMyCanceled -> CANCELED
                R.id.chipMyDone -> DONE
                else -> ALL
            }
            viewModel.setStateQuery(state)
        }
        adapter.addLoadStateListener { progressCallback?.setInProgress(it.refresh is LoadState.Loading || it.append is LoadState.Loading) }
    }

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }

        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.myOrdersLiveData) { adapter.submitData(lifecycle, it) }
    }

}