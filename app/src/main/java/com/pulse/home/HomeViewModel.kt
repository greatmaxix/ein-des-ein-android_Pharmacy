package com.pulse.home

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.chat.model.chat.ChatItem.Companion.STATUS_CLOSED
import com.pulse.core.general.SingleLiveEvent
import com.pulse.core.network.ResponseWrapper
import com.pulse.home.HomeFragmentDirections.Companion.globalToChatType
import com.pulse.home.repository.HomeRepository
import com.pulse.model.category.Category
import com.pulse.product.BaseProductViewModel
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
                try {
                    val activeChats = repository.getActiveChats()
                    val chatItem = activeChats.firstOrNull()
                    if (chatItem?.status != null && chatItem.status != STATUS_CLOSED) {
                        globalToChat(chatItem)
                    } else {
                        repository.clearSavedChatId()
                        globalToChatType()
                    }
                } catch (e: Exception) {
                    repository.clearSavedChatId()
                    globalToChatType()
                }
            }
            _progressLiveData.postValue(false)
            _directionLiveData.postValue(direction)
        }
    }
}