package com.pulse.components.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.pulse.components.chat.adapter.ChatMessageAdapter
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.components.chat.repository.ChatMessagesRemoteMediator
import com.pulse.components.chat.repository.ChatRepository
import com.pulse.components.chat.repository.ChatStubPagingSource
import com.pulse.components.product.model.Product
import com.pulse.components.product.repository.ProductRepository
import com.pulse.components.user.wishlist.repository.WishRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.getMultipartBody
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.data.remote.model.chat.SendReviewRequest
import com.pulse.util.Constants
import com.pulse.util.ImageFileUtil
import kotlinx.coroutines.flow.onEach
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
    private val isUserLoggedIn = repository.isUserLoggedIn.falseIfNull()
    val inputFieldState = StateEventFlow(isUserLoggedIn)
    val lastMessageState = repository.getLastMessageFlow(chatId).onEach {
        checkUserLoggedIn(it?.messageType == ChatMessageAdapter.TYPE_END_CHAT)
    }
    val chatFlow = repository.getChatFlow(chatId)
    val wishEvent = SingleShotEvent<Int>()
    val closeChatResultEvent = SingleShotEvent<Unit>()
    val endChatResultEvent = SingleShotEvent<Unit>()
    val productLiteState = StateEventFlow<Product?>(null)
    private var wishToSave: Pair<MessageItem, Int>? = null
    private val defaultPagingConfig by lazy { PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = false) }

    @ExperimentalPagingApi
    val chatMessagesState by lazy {
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
    }

    val tempPhotoFile = File(context.externalCacheDir, Constants.TEMP_PHOTO_FILE_NAME)

    private fun checkUserLoggedIn(isEndMessage: Boolean = false) {
        inputFieldState.postState(isUserLoggedIn && !isEndMessage)
    }

    fun sendMessage(message: String) = viewModelScope.execute {
        repository.sendMessage(chatId, message)
    }

    fun sendPhoto(uri: Uri) = viewModelScope.execute {
        ImageFileUtil.saveImageByUriToFile(context, tempPhotoFile, uri)
        ImageFileUtil.compressImage(context, tempPhotoFile, uri)

        val multipart = tempPhotoFile.getMultipartBody("file")
        val response = repository.uploadImage(multipart)
        tempPhotoFile.delete()
        repository.sendImageMessage(chatId, response.uuid)
    }

    fun sendReview(rating: Int, tags: List<String>?, note: String?) = viewModelScope.execute {
        val request = SendReviewRequest(rating, tags, note)
        repository.sendReview(chatId, request)
        endChatResultEvent.offerEvent(Unit)
    }

    fun closeChat() = viewModelScope.execute {
        repository.closeChat(chatId)
        closeChatResultEvent.offerEvent(Unit)
    }

    fun resumeChat() = viewModelScope.execute {
        repository.continueChat(chatId)
    }

    fun getProductInfo(globalProductId: Int) = viewModelScope.execute {
        val response = repositoryProduct.productById(globalProductId)
        saveToRecentlyViewedAndProceed(response.data.item)
    }

    private suspend fun saveToRecentlyViewedAndProceed(product: Product) {
        repositoryProduct.saveRecentlyViewed(product)
        productLiteState.postState(product)
    }

    fun setOrRemoveWish(setOrRemove: Pair<MessageItem, Int>) = viewModelScope.execute {
        repositoryWish.setOrRemoveWish(!(setOrRemove.first.product!!.isInWish) to setOrRemove.second)
        wishEvent.offerEvent(setOrRemove.second)
        wishToSave = null
    }

    fun checkIsWishSaved() {
        wishToSave?.let(::setOrRemoveWish)
    }
}