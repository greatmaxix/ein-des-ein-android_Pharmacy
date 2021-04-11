package com.pulse.components.chatType

import androidx.lifecycle.viewModelScope
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chatType.model.ChatType
import com.pulse.components.chatType.repository.ChatTypeRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow

class ChatTypeViewModel(private val repository: ChatTypeRepository) : BaseViewModel() {

    val chatTypeState = StateEventFlow<ChatType?>(null)
    val newChatEvent = SingleShotEvent<ChatItem>()

    fun setChatType(type: ChatType) = chatTypeState.postState(type)

    fun createChat() = viewModelScope.execute {
        newChatEvent.offerEvent(repository.createChat(chatTypeState.value?.value ?: ""))
    }
}