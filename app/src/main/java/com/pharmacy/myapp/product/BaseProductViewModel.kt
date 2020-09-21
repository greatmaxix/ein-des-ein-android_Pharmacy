package com.pharmacy.myapp.product

import androidx.lifecycle.LiveData
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.product.repository.ProductRepository
import com.pharmacy.myapp.user.repository.UserRepository
import com.pharmacy.myapp.user.wishlist.repository.WishRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseProductViewModel : BaseViewModel(), KoinComponent {

    private val repositoryWish by inject<WishRepository>()
    private val repositoryUser by inject<UserRepository>()
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
        launchIO {
            _progressLiveData.postValue(true)
            when (val response = repositoryProduct.productById(globalProductId)) {
                is Success -> {
                    val lastProduct = repositoryProduct.getRecentlyViewed().firstOrNull()?.apply { primaryKey = 1 }
                    val currentProduct = response.value.data.item
                    if (lastProduct != currentProduct) {
                        repositoryProduct.saveRecentlyViewed(listOfNotNull(currentProduct, lastProduct))
                    }
                    _productLiveData.postValue(currentProduct)
                }
                is Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
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