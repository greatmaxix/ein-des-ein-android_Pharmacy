package com.pharmacy.myapp.devTools

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.devTools.DevToolsFragmentDirections.Companion.globalToCart
import com.pharmacy.myapp.devTools.DevToolsFragmentDirections.Companion.globalToCheckout
import com.pharmacy.myapp.devTools.DevToolsFragmentDirections.Companion.globalToGuestProfile
import com.pharmacy.myapp.devTools.DevToolsFragmentDirections.Companion.globalToOrder
import com.pharmacy.myapp.devTools.DevToolsFragmentDirections.Companion.globalToProductCard
import kotlinx.android.synthetic.main.fragment_dev_tools.*

class DevToolsFragment(private val viewModel: DevToolsViewModel) : BaseMVVMFragment(R.layout.fragment_dev_tools) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toProductCard.onClick {
            navController.navigate(globalToProductCard())
        }

        toCheckout.onClick {
            navController.navigate(globalToCheckout())
        }

        toCart.onClick {
            navController.navigate(globalToCart())
        }

        toOrder.onClick {
            navController.navigate(globalToOrder())
        }

        toGuestProfile.onClick {
            navController.navigate(globalToGuestProfile())
        }
    }
}