package com.pulse.components.product

import androidx.lifecycle.viewModelScope
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.home.repository.HomeRepository
import com.pulse.components.product.ProductFragmentDirections.Companion.globalToChatType
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProductViewModel(private val homeRepository: HomeRepository) : BaseProductViewModel() {

    fun performAskPharmacist() = viewModelScope.execute {
        val direction = if (!repositoryUser.isCustomerExist()) {
            globalToChat()
        } else {
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
        navEvent.postEvent(direction)
    }
}