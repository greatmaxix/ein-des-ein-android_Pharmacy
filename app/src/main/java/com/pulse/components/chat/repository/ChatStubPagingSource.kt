package com.pulse.components.chat.repository

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pulse.R
import com.pulse.components.chat.adapter.ChatMessageAdapter
import com.pulse.components.chat.model.message.MessageItem

class ChatStubPagingSource(private val context: Context) : PagingSource<Int, MessageItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageItem> {
        val list = mutableListOf(
            MessageItem.getStubItem(null, null, ChatMessageAdapter.TYPE_DATE_HEADER, -1),
            MessageItem.getStubItem(context.getString(R.string.chat_description_message1), null, ChatMessageAdapter.TYPE_MESSAGE_PHARMACY, -1),
            MessageItem.getStubItem(context.getString(R.string.chat_description_message2), null, ChatMessageAdapter.TYPE_MESSAGE_PHARMACY, -1),
            MessageItem.getStubItem(context.getString(R.string.chat_description_message3), null, ChatMessageAdapter.TYPE_MESSAGE_USER, -1),
            MessageItem.getStubItem(context.getString(R.string.chat_description_message4), null, ChatMessageAdapter.TYPE_MESSAGE_USER, -1),
            MessageItem.getStubItem(context.getString(R.string.chat_description_message5), null, ChatMessageAdapter.TYPE_MESSAGE_PHARMACY, -1),
            MessageItem.getStubItem(null, null, ChatMessageAdapter.TYPE_AUTH_BUTTON, -1)
        ).reversed()
        return LoadResult.Page(list, null, null)
    }

    override fun getRefreshKey(state: PagingState<Int, MessageItem>) = state.anchorPosition
}