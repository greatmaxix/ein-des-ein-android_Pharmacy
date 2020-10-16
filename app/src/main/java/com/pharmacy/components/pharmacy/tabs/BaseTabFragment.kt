package com.pharmacy.components.pharmacy.tabs

import androidx.annotation.LayoutRes
import com.pharmacy.components.pharmacy.PharmacyViewModel
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

abstract class BaseTabFragment(@LayoutRes private val layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val vm: PharmacyViewModel by lazy { requireParentFragment().getViewModel() }

    protected fun addProduct(globalProductId: Int) = vm.addToCart(globalProductId)

    companion object {
        const val PHARMACY_KEY = "pharmacyKey"
    }
}