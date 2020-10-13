package com.pharmacy.devTools

import android.os.Bundle
import android.view.View
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.onClick
import com.pharmacy.devTools.DevToolsFragmentDirections.Companion.globalToPayments
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