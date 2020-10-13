package com.pharmacy.pharmacy.tabs

import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import com.pharmacy.R
import com.pharmacy.core.base.fragment.dialog.AlertDialogFragment
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.onNavDestinationSelected
import com.pharmacy.pharmacy.PharmacyViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

abstract class BaseTabFragment(@LayoutRes private val layoutResourceId: Int) : BaseMVVMFragment(layoutResourceId) {

    protected val viewModel: PharmacyViewModel by lazy { requireParentFragment().getViewModel() }

    override fun onBindLiveData() {
        observe(viewModel.cartNotifyLiveData) { notifyCardAdded() }
    }

    protected fun addProductToCart(globalProductId: Int) = viewModel.addToCart(globalProductId)

    private fun notifyCardAdded() {
        AlertDialogFragment.newInstance(null, getString(R.string.productAddedToCart), getString(R.string.cart), getString(R.string.stay))
            .apply {
                setNegativeListener { dialog, _ -> dialog.dismiss() }
                setPositiveListener { dialog, _ ->
                    requireParentFragment().findNavController().onNavDestinationSelected(R.id.nav_cart, null, R.id.nav_cart, true)
                    dialog.dismiss()
                }
            }.show(parentFragmentManager)
    }

    companion object {
        const val PHARMACY_KEY = "pharmacyKey"
    }
}