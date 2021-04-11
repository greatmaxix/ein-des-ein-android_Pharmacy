package com.pulse.components.product

import androidx.lifecycle.viewModelScope
import com.pulse.R
import com.pulse.components.product.model.Product
import com.pulse.components.product.repository.ProductRepository
import com.pulse.components.user.repository.CustomerRepository
import com.pulse.components.user.wishlist.repository.WishRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.data.GeneralException
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
abstract class BaseProductViewModel : BaseViewModel(), KoinComponent {

    private val repositoryWish by inject<WishRepository>()
    protected val repositoryUser by inject<CustomerRepository>()
    private val repositoryProduct by inject<ProductRepository>()

    val productLiteEvent = SingleShotEvent<Product>()
    val recentlyViewedState = StateEventFlow<List<Product>>(listOf())
    val wishEvent = SingleShotEvent<Int>()

    private var wishToSave: Pair<Boolean, Int>? = null

    fun checkIsWishSaved() {
        wishToSave?.let(::setOrRemoveWish)
    }

    fun getProductInfo(globalProductId: Int) = viewModelScope.execute {
        saveToRecentlyViewedAndProceed(repositoryProduct.productById(globalProductId).data.item)
    }

    private suspend fun saveToRecentlyViewedAndProceed(product: Product) {
        repositoryProduct.saveRecentlyViewed(product)
        productLiteEvent.offerEvent(product)
    }

    fun setOrRemoveWish(setOrRemove: Pair<Boolean, Int>) = viewModelScope.execute {
        if (repositoryUser.isCustomerExist()) {
            repositoryWish.setOrRemoveWish(setOrRemove.first to setOrRemove.second)
            wishEvent.offerEvent(setOrRemove.second)
            wishToSave = null
        } else {
            wishToSave = setOrRemove
            throw GeneralException("Error adding product to wish", resId = R.string.forAddingProduct, null, null)
        }
    }

    suspend fun getRecentlyViewed() {
        recentlyViewedState.postState(repositoryProduct.getRecentlyViewed())
    }

    companion object {
        const val PAGE_SIZE = 20
        const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}