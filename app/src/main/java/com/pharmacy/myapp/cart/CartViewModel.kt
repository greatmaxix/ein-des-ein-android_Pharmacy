package com.pharmacy.myapp.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.R
import com.pharmacy.myapp.cart.model.CartItem
import com.pharmacy.myapp.cart.repository.CartRepository
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper

class CartViewModel(private val repository: CartRepository) : BaseViewModel() {

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _errorLiveData by lazy { SingleLiveEvent<Int>() }
    val errorLiveData: LiveData<Int> by lazy { _errorLiveData }

    private val _cartItemLiveData by lazy { SingleLiveEvent<List<CartItem>>() }
    val cartItemLiveData: LiveData<List<CartItem>> by lazy { _cartItemLiveData }

    private val _removeItemLiveData by lazy { SingleLiveEvent<Int>() }
    val removeItemLiveData: LiveData<Int> by lazy { _removeItemLiveData }

    fun removeProductFromCart(productId: Int) {
        _progressLiveData.value = true
        launchIO {
            when (val response = repository.removeProductFromCart(productId)) {
                is ResponseWrapper.Success -> _removeItemLiveData.postValue(productId)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    fun cartOrAuth() {
        launchIO {
            if (repository.isCustomerExist()) retrieveUserCart() else _errorLiveData.postValue(R.string.forCheckCart)
        }
    }

    private suspend fun retrieveUserCart() {
        _progressLiveData.postValue(true)
        when (val response = repository.getCartProducts()) {
            is ResponseWrapper.Success -> _cartItemLiveData.postValue(response.value.data.items)
            is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
        }
        _progressLiveData.postValue(false)
    }
}