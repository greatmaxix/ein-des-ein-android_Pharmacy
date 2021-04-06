package com.pulse.components.pharmacy.tabs

import androidx.annotation.LayoutRes
import com.pulse.components.pharmacy.PharmacyViewModel
import com.pulse.core.base.mvvm.BaseMVVMFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

abstract class BaseTabFragment(@LayoutRes private val layoutResourceId: Int) : BaseMVVMFragment<PharmacyViewModel>(layoutResourceId, PharmacyViewModel::class) {

    override val viewModel: PharmacyViewModel by lazy { requireParentFragment().getViewModel() }

    protected fun addProduct(globalProductId: Int) = viewModel.addToCart(globalProductId)

    companion object {

        const val PHARMACY_KEY = "pharmacyKey"
    }
}