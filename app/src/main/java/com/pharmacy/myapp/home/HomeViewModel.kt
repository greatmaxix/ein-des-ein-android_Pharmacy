package com.pharmacy.myapp.home

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.data.remote.model.chat.ChatItem.Companion.STATUS_CLOSED
import com.pharmacy.myapp.data.remote.model.chat.ChatItem.Companion.STATUS_RATED
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.fromHomeToChatType
import com.pharmacy.myapp.home.HomeFragmentDirections.Companion.globalToChat
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

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    fun performAskPharmacist() {
        launchIO {
            val direction = if (!repositoryUser.isCustomerExist()) {
                globalToChat()
            } else {
                _progressLiveData.postValue(true)
                val chatItem = repository.getCurrentChat()
                if (chatItem?.status != null && chatItem.status != STATUS_CLOSED && chatItem.status != STATUS_RATED) {
                    globalToChat(chatItem)
                } else {
                    fromHomeToChatType()
                }
            }
            _progressLiveData.postValue(false)
            _directionLiveData.postValue(direction)
        }
    }
}