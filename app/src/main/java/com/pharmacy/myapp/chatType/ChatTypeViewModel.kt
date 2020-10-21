package com.pharmacy.myapp.chatType

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pharmacy.myapp.chatType.model.ChatType
import com.pharmacy.myapp.chatType.repository.ChatTypeRepository
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent

class ChatTypeViewModel(private val repository: ChatTypeRepository) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _chatTypeLiveData by lazy { MutableLiveData<ChatType?>() }
    val chatTypeLiveData: LiveData<ChatType?> by lazy { _chatTypeLiveData }

    fun setChatType(type: ChatType) {
        _chatTypeLiveData.value = type
    }

    fun createChat() = requestLiveData {
        repository.createChat(chatTypeLiveData.value?.value ?: "")
    }
}