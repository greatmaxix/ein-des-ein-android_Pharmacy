package com.pharmacy.home

import androidx.lifecycle.LiveData
import com.pharmacy.core.general.SingleLiveEvent
import com.pharmacy.core.network.ResponseWrapper
import com.pharmacy.home.repository.HomeRepository
import com.pharmacy.model.category.Category
import com.pharmacy.product.BaseProductViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeViewModel(private val repository: HomeRepository) : BaseProductViewModel() {

    private val _categoriesLiveData by lazy { SingleLiveEvent<List<Category>>() }
    val categoriesLiveData: LiveData<List<Category>> by lazy { _categoriesLiveData }

    fun loadInitialData() {
        launchIO {
            when (val response = repository.getCategories()) {
                is ResponseWrapper.Success -> _categoriesLiveData.postValue(response.value)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
        }
        getRecentlyViewed()
    }

}