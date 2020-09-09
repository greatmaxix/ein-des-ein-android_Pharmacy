package com.pharmacy.myapp.product

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.showAlert
import com.pharmacy.myapp.produtcList.BaseProductViewModel

abstract class BaseProductFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) : BaseMVVMFragment(layoutResourceId) {

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData, ::errorOrDialog)
        observe(viewModel.wishLiteLiveData, ::notifyWish)
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIsWishSaved()
    }

    abstract fun notifyWish(globalProductId: Int)

    /**
     * Here we may write some custom logic for authorization process.
     * This method will be executed [errorOrDialog] method
     */
    protected open fun needToLogin() {
        //Optional
    }

    private fun errorOrDialog(@StringRes strResId: Int) {
        if (strResId == R.string.forAddingProduct) {
            showAlert(strResId) {
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { needToLogin() }
            }
        } else {
            messageCallback?.showError(strResId)
        }
    }

    companion object {
        const val PRODUCT_WISH_KEY = "productWishKey"
        const val PRODUCT_WISH_FIELD = "productWishField"
    }
}