package com.pharmacy.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.pharmacy.cart.repository.CartRepository
import com.pharmacy.core.base.mvvm.BaseViewModel
import com.pharmacy.data.GeneralException

class CartViewModel(private val repository: CartRepository) : BaseViewModel() {

    val cartItemLiveData = requestLiveData {
        if (repository.isCustomerExist()) repository.getCartProducts().items else throw GeneralException.needToLoginCart
    }


    private val _removeItemLiveData by lazy { MutableLiveData<Int>() }
    val removeItemLiveData = _removeItemLiveData.switchMap { productId ->
        requestLiveData {
            repository.removeProductFromCart(productId)
            productId
        }
    }

    fun removeProductFromCart(productId: Int) {
        _removeItemLiveData.value = productId
    }

    fun cartOrAuth() {
        launchIO {
            //if (repository.isCustomerExist()) retrieveUserCart() else _errorLiveData.postValue(R.string.forCheckCart)
        }
    }

    private suspend fun retrieveUserCart() {


        /* _progressLiveData.postValue(true)
         when (val response = repository.getCartProducts()) {
             is ResponseWrapper.Success -> _cartItemLiveData.postValue(response.value.data.items)
             is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
         }
         _progressLiveData.postValue(false)*/
    }
}