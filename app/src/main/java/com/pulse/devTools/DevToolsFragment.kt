package com.pulse.devTools

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.onClick
import com.pulse.devTools.DevToolsFragmentDirections.Companion.globalToPayments
import kotlinx.android.synthetic.main.fragment_dev_tools.*

class DevToolsFragment(private val viewModel: DevToolsViewModel) : BaseMVVMFragment(R.layout.fragment_dev_tools) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toProductCard.onClick {
            //navController.navigate(globalToProductCard())
        }

        toPayments.onClick {
            navController.navigate(globalToPayments())
        }
    }
}