package com.pulse.components.payments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.pulse.R
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.sharedGraphViewModel

abstract class PaymentsBaseFragment(@LayoutRes layoutResourceId: Int) : BaseToolbarFragment<PaymentsViewModel>(layoutResourceId, PaymentsViewModel::class) {

    override val viewModel: PaymentsViewModel by sharedGraphViewModel(R.id.graph_payments)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
    }
}