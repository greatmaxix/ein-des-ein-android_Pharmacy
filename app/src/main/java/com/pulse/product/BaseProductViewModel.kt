package com.pulse.product

import androidx.lifecycle.LiveData
import com.pulse.R
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.general.SingleLiveEvent
import com.pulse.core.network.ResponseWrapper.Error
import com.pulse.core.network.ResponseWrapper.Success
import com.pulse.product.model.Product
import com.pulse.product.repository.ProductRepository
import com.pulse.user.repository.CustomerRepository
import com.pulse.user.wishlist.repository.WishRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
abstract class BaseProductViewModel : BaseViewModel(), KoinComponent {

    private val repositoryWish by inject<WishRepository>()
    protected val repositoryUser by inject<CustomerRepository>()
    private val repositoryProduct by inject<ProductRepository>()

    protected val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    protected val _errorLiveData by lazy { SingleLiveEvent<Int>() }
    val errorLiveData: LiveData<Int> by lazy { _errorLiveData }

    private val _productLiveData by lazy { SingleLiveEvent<Product>() }
    val productLiteLiveData: LiveData<Product> by lazy { _productLiveData }

    private val _wishLiveData by lazy { SingleLiveEvent<Int>() }
    val wishLiteLiveData: LiveData<Int> by lazy { _wishLiveData }

    private val _recentlyViewedLiveData by lazy { SingleLiveEvent<List<Product>>() }
    val recentlyViewedLiveData: LiveData<List<Product>> by lazy { _recentlyViewedLiveData }

    private var wishToSave: Pair<Boolean, Int>? = null

    fun checkIsWishSaved() {
        wishToSave?.let(::setOrRemoveWish)
    }

    fun getProductInfo(globalProductId: Int) {
        _progressLiveData.value = true
        launchIO {
            when (val response = repositoryProduct.productById(globalProductId)) {
                is Success -> saveToRecentlyViewedAndProceed(response.value.data.item)
                is Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    private suspend fun saveToRecentlyViewedAndProceed(product: Product) {
        repositoryProduct.saveRecentlyViewed(product)
        _productLiveData.postValue(product)
    }

    fun setOrRemoveWish(setOrRemove: Pair<Boolean, Int>) {
        launchIO {
            if (repositoryUser.isCustomerExist()) {
                _progressLiveData.postValue(true)
                when (val response = repositoryWish.setOrRemoveWish(setOrRemove.first to setOrRemove.second)) {
                    is Success -> {
                        _wishLiveData.postValue(setOrRemove.second)
                        wishToSave = null
                    }
                    is Error -> _errorLiveData.postValue(response.errorResId)
                }
                _progressLiveData.postValue(false)
            } else {
                wishToSave = setOrRemove
                _errorLiveData.postValue(R.string.forAddingProduct)
            }
        }
    }

    fun getRecentlyViewed() {
        launchIO { _recentlyViewedLiveData.postValue(repositoryProduct.getRecentlyViewed()) }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}