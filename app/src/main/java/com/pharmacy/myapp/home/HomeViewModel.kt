package com.pharmacy.myapp.home

import androidx.lifecycle.LiveData
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.home.repository.HomeRepository
import com.pharmacy.myapp.model.category.Category
import com.pharmacy.myapp.product.BaseProductViewModel
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