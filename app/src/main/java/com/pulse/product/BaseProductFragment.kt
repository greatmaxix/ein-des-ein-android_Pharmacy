package com.pulse.product

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.pulse.MainGraphDirections.Companion.globalToProductCard
import com.pulse.R
import com.pulse.categories.search.CategoriesSearchFragment
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.showAlert
import com.pulse.home.HomeFragment
import com.pulse.product.model.Product
import com.pulse.scanner.ScannerFragment
import com.pulse.scanner.ScannerListFragment
import com.pulse.search.SearchFragment
import com.pulse.search.SearchFragmentDirections.Companion.fromSearchToProduct
import com.pulse.user.wishlist.WishFragment
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseProductFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) : BaseMVVMFragment(layoutResourceId) {

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData, ::errorOrAuth)
        observe(viewModel.wishLiteLiveData, ::notifyWish)
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.productLiteLiveData) { navController.navigate(getNavDirection(it)) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIsWishSaved()
    }

    abstract fun notifyWish(globalProductId: Int)

    /*
     * Here we may write some custom logic for authorization process.
     * This method will be executed [errorOrDialog] method
     */
    protected open fun needToLogin() {
        //Optional
    }

    private fun errorOrAuth(@StringRes strResId: Int) {
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

    private fun getNavDirection(product: Product) = when (this) {
        is WishFragment -> globalToProductCard(product)
        is SearchFragment -> fromSearchToProduct(product)
        is ScannerFragment -> globalToProductCard(product)
        is ScannerListFragment -> globalToProductCard(product)
        is CategoriesSearchFragment -> globalToProductCard(product)
        is HomeFragment -> globalToProductCard(product)
        else -> throw Exception("Add new instance to base product")
    }

    companion object {
        const val PRODUCT_WISH_KEY = "productWishKey"
        const val PRODUCT_WISH_FIELD = "productWishField"
    }
}