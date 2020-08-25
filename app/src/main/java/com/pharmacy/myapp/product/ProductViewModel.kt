package com.pharmacy.myapp.product

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.product.repository.ProductRepository
import timber.log.Timber

class ProductViewModel(private val repository: ProductRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }


    init {
        launchIO {
            Timber.e(repository.productById(743).toString())
        }

    }
}