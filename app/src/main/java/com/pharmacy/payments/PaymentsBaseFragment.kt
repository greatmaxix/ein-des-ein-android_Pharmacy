package com.pharmacy.payments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.sharedGraphViewModel

abstract class PaymentsBaseFragment(@LayoutRes layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val viewModel: PaymentsViewModel by sharedGraphViewModel(R.id.graph_payments)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData, navController::navigate)

        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }
}