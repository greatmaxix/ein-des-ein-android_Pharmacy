package com.pharmacy.myapp.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.pharmacy.myapp.chat.model.ChatMessage
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.data.remote.DummyData
import com.pharmacy.myapp.util.Constants
import timber.log.Timber
import java.io.File
import java.time.LocalDateTime
import java.util.*

class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository
) : BaseViewModel() {

    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _isUserLoggedInLiveData by lazy { SingleLiveEvent<Boolean>() }
    val isUserLoggedInLiveData: LiveData<Boolean> by lazy { _isUserLoggedInLiveData }

    private val _chatMessagesLiveData by lazy { MutableLiveData<MutableList<ChatMessage>>() }
    val chatMessagesLiveData: LiveData<MutableList<ChatMessage>> by lazy { _chatMessagesLiveData }

    val tempPhotoFile = File(context.externalCacheDir, Constants.TEMP_PHOTO_FILE_NAME)

    fun checkUserLoggedIn() {
        _chatMessagesLiveData.value = mutableListOf()
        val userLoggedIn = repository.isUserLoggedIn
        _isUserLoggedInLiveData.value = userLoggedIn

        mockPharmacyResponse(userLoggedIn)
    }

    fun sendMessage(message: String) {
        addMessageToChatList(message)

        // TODO send message to server
    }

    private fun addMessageToChatList(message: String? = null, images: MutableList<String>? = null) {
        val list = chatMessagesLiveData.value ?: arrayListOf()
        if (list.isEmpty()) list.add(0, ChatMessage.DateHeader(LocalDateTime.now()))
        if (!message.isNullOrBlank()) {
            list.add(0, ChatMessage.UserMessage(message))
        } else if (!images.isNullOrEmpty()) {
            list.add(0, ChatMessage.Attachment(images))
        }

        _chatMessagesLiveData.value = list

        mockPharmacyResponse()
    }

    fun sendPhotos(uriList: List<Uri>) {
        addMessageToChatList(images = uriList.map { it.toString() }.toMutableList())
        // TODO send photos
        Timber.e(uriList.toString())
    }

    // TODO remove
    @Deprecated("Mock data method")
    private fun mockPharmacyResponse(startMessages: Boolean = false) {
        val list = chatMessagesLiveData.value ?: arrayListOf()
        if (startMessages) {
            list.add(0, ChatMessage.DateHeader(LocalDateTime.now()))
            list.add(0, ChatMessage.PharmacyMessage("Добрый день! Меня зовут Эстер"))
            list.add(0, ChatMessage.PharmacyMessage("Чем я могу Вам помочь?"))
        } else {
            if (list.size > 10 && !list.contains(ChatMessage.EndChat)) {
                list.add(0, ChatMessage.PharmacyMessage("У Вас есть еще какие либо вопросы?"))
                list.add(0, ChatMessage.EndChat)
            } else {
                if (Random().nextBoolean()) {
                    list.add(0, ChatMessage.PharmacyMessage(DummyData.pharmacyResponses.random()))
                } else {
                    list.add(0, ChatMessage.Product(DummyData.getRecommended().random()))
                }
            }
        }

        _chatMessagesLiveData.value = list
    }

    fun setNotAuthorizedChatMessages(list: MutableList<ChatMessage>) {
        _chatMessagesLiveData.value = list
    }

    fun removeEndChatMessage() {
        val list = chatMessagesLiveData.value ?: arrayListOf()
        list.removeIf { it is ChatMessage.EndChat }
        _chatMessagesLiveData.value = list
    }

    fun sendReview(rating: Int, note: String?) {
        Timber.e("SEND REVIEW $rating >>> $note")
    }
}