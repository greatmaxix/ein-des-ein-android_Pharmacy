package com.pulse.components.product

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.home.repository.HomeRepository
import com.pulse.components.product.ProductFragmentDirections.Companion.globalToChatType
import com.pulse.core.general.SingleLiveEvent
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProductViewModel(private val homeRepository: HomeRepository) : BaseProductViewModel() {

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    fun performAskPharmacist() {
        launchIO {
            val direction = if (!repositoryUser.isCustomerExist()) {
                globalToChat()
            } else {
                _progressLiveData.postValue(true)
                try {
                    val activeChats = homeRepository.getActiveChats()
                    val chatItem = activeChats.firstOrNull()
                    if (chatItem?.status != null && chatItem.status != ChatItem.STATUS_CLOSED) {
                        globalToChat(chatItem)
                    } else {
                        homeRepository.clearSavedChatId()
                        globalToChatType()
                    }
                } catch (e: Exception) {
                    homeRepository.clearSavedChatId()
                    globalToChatType()
                }
            }
            _progressLiveData.postValue(false)
            _directionLiveData.postValue(direction)
        }
    }
}