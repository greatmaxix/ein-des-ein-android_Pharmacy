package com.pulse.components.product

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import com.pulse.MainGraphDirections.Companion.globalToProductCard
import com.pulse.R
import com.pulse.components.categories.search.CategoriesSearchFragment
import com.pulse.components.home.HomeFragment
import com.pulse.components.product.model.Product
import com.pulse.components.scanner.ScannerFragment
import com.pulse.components.scanner.ScannerListFragment
import com.pulse.components.search.SearchFragment
import com.pulse.components.search.SearchFragmentDirections.Companion.fromSearchToProduct
import com.pulse.components.user.wishlist.WishFragment
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.observe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension
import kotlin.reflect.KClass

@KoinApiExtension
@ExperimentalCoroutinesApi
abstract class BaseProductFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, viewModelClass: KClass<VM>) :
    BaseToolbarFragment<VM>(layoutResourceId, viewModelClass) {

    abstract fun notifyWish(globalProductId: Int)

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.wishEvent.events, ::notifyWish)
        observe(viewModel.productLiteEvent.events) { navController.navigate(getNavDirection(it)) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkIsWishSaved()
    }

    /*
     * Here we may write some custom logic for authorization process.
     * This method will be executed [errorOrDialog] method
     */
    protected open fun needToLogin() {
        //Optional
    }

    private fun errorOrAuth(@StringRes strResId: Int) {
        if (strResId == R.string.forAddingProduct) {
            uiHelper.showDialog(getString(strResId)) {
                positive = getString(R.string.signIn)
                negative = getString(R.string.cancel)
                positiveAction = { needToLogin() }
            }
        } else {
            uiHelper.showMessage(getString(strResId))
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