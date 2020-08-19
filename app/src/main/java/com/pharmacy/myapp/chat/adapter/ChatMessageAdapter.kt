package com.pharmacy.myapp.chat.adapter

import android.view.ViewGroup
import com.pharmacy.myapp.chat.adapter.viewHolder.*
import com.pharmacy.myapp.chat.model.ChatMessage
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder

class ChatMessageAdapter(private val listener: (Action) -> Unit) : BaseRecyclerAdapter<ChatMessage, BaseViewHolder<ChatMessage>>() {

    fun setList(list: MutableList<ChatMessage>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ChatMessage> =
        when (viewType) {
            TYPE_MESSAGE_USER -> UserMessageViewHolder.newInstance(parent)
            TYPE_MESSAGE_PHARMACY -> PharmacyMessageViewHolder.newInstance(parent)
            TYPE_DATE_HEADER -> DateHeaderViewHolder.newInstance(parent)
            TYPE_ATTACHMENT -> AttachmentViewHolder.newInstance(parent)
            TYPE_PRODUCT -> ProductViewHolder.newInstance(parent)
            TYPE_AUTH_BUTTON -> AuthorizeButtonViewHolder.newInstance(parent, listener)
            else -> EndChatViewHolder.newInstance(parent, listener)
        }

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType
    }

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