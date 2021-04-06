package com.pulse.components.home

import androidx.lifecycle.viewModelScope
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.components.chat.model.chat.ChatItem.Companion.STATUS_CLOSED
import com.pulse.components.home.HomeFragmentDirections.Companion.globalToChatType
import com.pulse.components.home.repository.HomeRepository
import com.pulse.components.product.BaseProductViewModel
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.model.category.Category
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeViewModel(private val repository: HomeRepository) : BaseProductViewModel() {

    val categoriesState = StateEventFlow<List<Category>>(listOf())

    fun loadInitialData() = viewModelScope.launch {
        categoriesState.postState(repository.getCategories())
        getRecentlyViewed()
    }

    fun performAskPharmacist() = viewModelScope.execute {
        val direction = if (!repositoryUser.isCustomerExist()) {
            globalToChat()
        } else {
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
        navEvent.postEvent(direction)
    }
}