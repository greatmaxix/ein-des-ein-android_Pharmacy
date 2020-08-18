package com.pharmacy.myapp.payments

import androidx.annotation.LayoutRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel

abstract class PaymentsBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val viewModel: PaymentsViewModel by sharedGraphViewModel(R.id.graph_payments)

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.directionLiveData.observeExt(navController::navigate)
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
    }
}