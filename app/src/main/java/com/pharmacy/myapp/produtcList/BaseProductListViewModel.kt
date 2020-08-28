package com.pharmacy.myapp.produtcList

import androidx.lifecycle.LiveData
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.product.repository.ProductRepository
import com.pharmacy.myapp.user.wishlist.repository.WishRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseProductListViewModel : BaseViewModel(), KoinComponent {

    private val repositoryWish by inject<WishRepository>()
    private val repositoryProduct by inject<ProductRepository>()

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    protected val _errorLiveData by lazy { SingleLiveEvent<Int>() }
    val errorLiveData: LiveData<Int> by lazy { _errorLiveData }

    private val _productLiveData by lazy { SingleLiveEvent<Product>() }
    val productLiteLiveData: LiveData<Product> by lazy { _productLiveData }

    private val _wishLiveData by lazy { SingleLiveEvent<Int>() }
    val wishLiteLiveData: LiveData<Int> by lazy { _wishLiveData }


    fun getProductInfo(globalProductId: Int) {
        launchIO {
            _progressLiveData.postValue(true)
            when (val response = repositoryProduct.productById(globalProductId)) {
                is ResponseWrapper.Success -> _productLiveData.postValue(response.value.data.item)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    open fun setOrRemoveWish(setOrRemove: Triple<Boolean, Int, Int>) {
        launchIO {
            _progressLiveData.postValue(true)
            when (val response = repositoryWish.setOrRemoveWish(setOrRemove.first to setOrRemove.second)) {
                is ResponseWrapper.Success -> _wishLiveData.postValue(setOrRemove.third)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val INIT_LOAD_SIZE = PAGE_SIZE * 2
    }
}