package com.pharmacy.myapp.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.pharmacy.myapp.chat.repository.ChatMessagesRemoteMediator
import com.pharmacy.myapp.chat.repository.ChatRepository
import com.pharmacy.myapp.chat.repository.ChatStubPagingSource
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.getMultipartBody
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.data.remote.model.chat.ChatItem
import com.pharmacy.myapp.util.Constants
import com.pharmacy.myapp.util.ImageFileUtil
import org.koin.core.component.KoinApiExtension
import timber.log.Timber
import java.io.File

@KoinApiExtension
class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    private val chat: ChatItem?
) : BaseViewModel() {

    private val chatId = chat?.id ?: -1
    private val _errorLiveData by lazy { SingleLiveEvent<String>() }
    val errorLiveData: LiveData<String> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _isUserLoggedInLiveData by lazy { SingleLiveEvent<Boolean>() }
    val isUserLoggedInLiveData: LiveData<Boolean> by lazy { _isUserLoggedInLiveData }

    private val defaultPagingConfig by lazy { PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 1, initialLoadSize = Constants.PAGE_SIZE / 2) }

    val lastMessageLiveData = repository.getLastMessageLiveData(chatId)
        .distinctUntilChanged()

    @ExperimentalPagingApi
    val chatMessagesLiveData by lazy {
        if (repository.isUserLoggedIn && chat != null) {
            Pager(
                config = defaultPagingConfig,
                remoteMediator = ChatMessagesRemoteMediator(repository, errorHandler, chat),
                pagingSourceFactory = { repository.getMessagePagingSource(chat.id) }
            )
        } else {
            Pager(
                config = defaultPagingConfig,
                pagingSourceFactory = { ChatStubPagingSource(context) }
            )
        }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    val tempPhotoFile = File(context.externalCacheDir, Constants.TEMP_PHOTO_FILE_NAME)

    fun checkUserLoggedIn() {
        val userLoggedIn = repository.isUserLoggedIn
        _isUserLoggedInLiveData.value = userLoggedIn
    }

    fun sendMessage(message: String) = requestLiveData {
        repository.sendMessage(chatId, message)
    }

    fun sendPhoto(uri: Uri) = requestLiveData {
        ImageFileUtil.saveImageByUriToFile(context, tempPhotoFile, uri)
        ImageFileUtil.compressImage(context, tempPhotoFile, uri)

        val multipart = tempPhotoFile.getMultipartBody("file")
        val response = repository.uploadImage(multipart)
        tempPhotoFile.delete()
        repository.sendImageMessage(chatId, response.uuid)
    }

    fun removeEndChatMessage() {
        launchIO {
            repository.removeEndChatMessage(chatId)
        }
    }

    fun sendReview(rating: Int, note: String?) {
        // TODO implement flow
        Timber.e("SEND REVIEW $rating >>> $note")
    }

    fun closeChat() = requestLiveData {
        repository.closeChat(chatId)
    }
}