package com.pharmacy.myapp.chat.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.myapp.chat.adapter.viewHolder.*
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder

class ChatMessageAdapter(private val listener: (Action) -> Unit) : PagingDataAdapter<MessageItem, BaseViewHolder<MessageItem>>(ChatMessagesDiff) {

    override fun onBindViewHolder(holder: BaseViewHolder<MessageItem>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_MESSAGE_USER -> UserMessageViewHolder.newInstance(parent)
            TYPE_MESSAGE_PHARMACY -> PharmacyMessageViewHolder.newInstance(parent)
            TYPE_DATE_HEADER -> DateHeaderViewHolder.newInstance(parent)
            TYPE_ATTACHMENT -> AttachmentViewHolder.newInstance(parent)
            TYPE_PRODUCT -> ProductViewHolder.newInstance(parent)
            TYPE_AUTH_BUTTON -> AuthorizeButtonViewHolder.newInstance(parent, listener)
            else -> EndChatViewHolder.newInstance(parent, listener)
        }

    override fun getItemViewType(position: Int) = getItem(position)?.messageType ?: -1

    companion object {

        const val TYPE_MESSAGE_USER = 1
        const val TYPE_MESSAGE_PHARMACY = 2
        const val TYPE_DATE_HEADER = 3
        const val TYPE_ATTACHMENT = 4
        const val TYPE_PRODUCT = 5
        const val TYPE_AUTH_BUTTON = 6
        const val TYPE_END_CHAT = 7
    }

    enum class Action {
        END_CHAT,
        RESUME_CHAT,
        AUTHORIZE
    }
}