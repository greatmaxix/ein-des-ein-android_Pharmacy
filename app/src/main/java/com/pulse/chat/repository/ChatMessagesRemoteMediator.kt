package com.pulse.chat.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pulse.chat.adapter.ChatMessageAdapter
import com.pulse.chat.model.chat.ChatItem
import com.pulse.chat.model.message.MessageItem
import com.pulse.chat.model.remoteKeys.RemoteKeys
import com.pulse.data.GeneralErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import timber.log.Timber
import java.time.ZoneOffset

@KoinApiExtension
@ExperimentalPagingApi
class ChatMessagesRemoteMediator(private val repository: ChatRepository, private val errorHandler: GeneralErrorHandler, private val chat: ChatItem) :
    RemoteMediator<Int, MessageItem>() {

    private var userUuid: String? = null

    override suspend fun initialize(): InitializeAction =
        if (repository.getLastMessage(chat.id) == null) InitializeAction.LAUNCH_INITIAL_REFRESH else InitializeAction.SKIP_INITIAL_REFRESH

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MessageItem>): MediatorResult = withContext(Dispatchers.IO) {
        try {
            val remoteKeys = getRemoteKeys(loadType, state)
            Timber.d("Load > ${loadType.name} : remoteKeys > ${if (remoteKeys.isEmpty) "EMPTY" else if (remoteKeys.isError) "ERROR" else remoteKeys.toString()}")

            if (!remoteKeys.isError) {
                if (userUuid == null) userUuid = repository.getCustomerUuid()
                val beforeNumber: Int? = null
                var afterNumber: Int? = null

                val startNumber = chat.lastMessage?.messageNumber
                if (loadType == LoadType.REFRESH && startNumber != null && startNumber > state.config.pageSize) {
                    afterNumber = startNumber - state.config.pageSize
                } else if (loadType == LoadType.APPEND && remoteKeys.nextNumber != null) afterNumber = remoteKeys.nextNumber - 1

                val response = repository.fetchMessagesList(chat.id, state.config.pageSize, afterNumber, beforeNumber)
                var items = response.items.apply { forEach { it.updateMessageType(userUuid) } }
                if (items.isNotEmpty()) {
                    val messagesList = arrayListOf<MessageItem>()
                    items = items.sortedBy { it.createdAt.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() }
                    var lastItem = if (loadType != LoadType.REFRESH) repository.getLastMessage(chat.id) else null
                    items.forEachIndexed { index, messageItem ->
                        if ((index == 0 && lastItem == null) || (lastItem != null && lastItem?.createdAt?.dayOfMonth != messageItem.createdAt.dayOfMonth)) {
                            val header = MessageItem.getStubItem(null, messageItem, ChatMessageAdapter.TYPE_DATE_HEADER, chat.id)
                            if (!repository.isHeaderExist(chat.id, header.createdAt)) messagesList.add(header)
                        }
                        messagesList.add(messageItem)
                        lastItem = messageItem
                    }
                    if (loadType == LoadType.REFRESH) repository.clearMessages(chat.id)
                    repository.insertMessagesWithKeys(messagesList)

                    return@withContext MediatorResult.Success(endOfPaginationReached = messagesList.size < state.config.pageSize)
                }
            }
            MediatorResult.Success(endOfPaginationReached = true)

        } catch (e: Exception) {
            MediatorResult.Error(errorHandler.checkThrowable(e))
        }
    }

    private suspend fun getRemoteKeys(loadType: LoadType, state: PagingState<Int, MessageItem>): RemoteKeys {
        return when (loadType) {
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state) ?: RemoteKeys.emptyInstance()
            LoadType.PREPEND -> RemoteKeys.errorInstance()
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys?.nextNumber == null) RemoteKeys.errorInstance() else remoteKeys
            }
            else -> RemoteKeys.emptyInstance()
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MessageItem>): RemoteKeys? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull { it.messageNumber != -1 }?.let { message -> repository.getRemoteKeys(message.id) }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MessageItem>): RemoteKeys? =
        state.anchorPosition?.let { position -> state.closestItemToPosition(position)?.id?.let { messageId -> repository.getRemoteKeys(messageId) } }
}