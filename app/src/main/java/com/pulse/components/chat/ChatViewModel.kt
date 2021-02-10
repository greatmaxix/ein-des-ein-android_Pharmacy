package com.pulse.components.chat

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
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.components.chat.repository.ChatMessagesRemoteMediator
import com.pulse.components.chat.repository.ChatRepository
import com.pulse.components.chat.repository.ChatStubPagingSource
import com.pulse.components.product.model.Product
import com.pulse.components.product.repository.ProductRepository
import com.pulse.components.user.wishlist.repository.WishRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.getMultipartBody
import com.pulse.core.general.SingleLiveEvent
import com.pulse.core.network.ResponseWrapper
import com.pulse.data.remote.model.chat.SendReviewRequest
import com.pulse.util.Constants
import com.pulse.util.ImageFileUtil
import org.koin.core.component.KoinApiExtension
import java.io.File

@KoinApiExtension
class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    private val chat: ChatItem?,
    private val repositoryWish: WishRepository,
    private val repositoryProduct: ProductRepository
) : BaseViewModel() {

    private val chatId = chat?.id ?: -1
    private val _errorLiveData by lazy { SingleLiveEvent<Int>() }
    val errorLiveData: LiveData<Int> by lazy { _errorLiveData }

    private val _progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val progressLiveData: LiveData<Boolean> by lazy { _progressLiveData }

    private val _directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    val directionLiveData: LiveData<NavDirections> by lazy { _directionLiveData }

    private val _isUserLoggedInLiveData by lazy { SingleLiveEvent<Boolean>() }
    val isUserLoggedInLiveData: LiveData<Boolean> by lazy { _isUserLoggedInLiveData }

    private val defaultPagingConfig by lazy { PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false) }

    val lastMessageLiveData = repository.getLastMessageLiveData(chatId)
        .distinctUntilChanged()
    val chatLiveData = repository.getChatLiveData(chatId)
        .distinctUntilChanged()

    private var wishToSave: Pair<MessageItem, Int>? = null
    private val _wishLiveData by lazy { SingleLiveEvent<Int>() }
    val wishLiteLiveData: LiveData<Int> by lazy { _wishLiveData }
    private val _productLiveData by lazy { SingleLiveEvent<Product>() }
    val productLiteLiveData: LiveData<Product> by lazy { _productLiveData }

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

    fun sendReview(rating: Int, tags: List<String>?, note: String?) = requestLiveData {
        val request = SendReviewRequest(rating, tags, note)
        repository.sendReview(chatId, request)
    }

    fun closeChat() = requestLiveData {
        repository.closeChat(chatId)
    }

    fun resumeChat() = requestLiveData {
        repository.continueChat(chatId)
    }

    fun getProductInfo(globalProductId: Int) {
        _progressLiveData.value = true
        launchIO {
            when (val response = repositoryProduct.productById(globalProductId)) {
                is ResponseWrapper.Success -> saveToRecentlyViewedAndProceed(response.value.data.item)
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    private suspend fun saveToRecentlyViewedAndProceed(product: Product) {
        repositoryProduct.saveRecentlyViewed(product)
        _productLiveData.postValue(product)
    }

    fun setOrRemoveWish(setOrRemove: Pair<MessageItem, Int>) {
        launchIO {
            _progressLiveData.postValue(true)
            when (val response = repositoryWish.setOrRemoveWish(!(setOrRemove.first.product!!.isInWish) to setOrRemove.second)) {
                is ResponseWrapper.Success -> {
                    _wishLiveData.postValue(setOrRemove.second)
                    wishToSave = null
                }
                is ResponseWrapper.Error -> _errorLiveData.postValue(response.errorResId)
            }
            _progressLiveData.postValue(false)
        }
    }

    fun checkIsWishSaved() {
        wishToSave?.let(::setOrRemoveWish)
    }
}