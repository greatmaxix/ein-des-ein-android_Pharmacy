package com.pharmacy.myapp.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.adapter.ChatMessageAdapter
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_chat_end.view.*

class EndChatViewHolder(itemView: View, listener: (ChatMessageAdapter.Action) -> Unit) : BaseViewHolder<MessageItem>(itemView) {

    init {
        with(itemView) {
            btnResumeChat.setDebounceOnClickListener { listener.invoke(ChatMessageAdapter.Action.RESUME_CHAT) }
            btnEndChat.setDebounceOnClickListener { listener.invoke(ChatMessageAdapter.Action.END_CHAT) }
        }
    }

    override fun bind(item: MessageItem) {
        // no op
    }

    companion object {

        fun newInstance(parent: ViewGroup, listener: (ChatMessageAdapter.Action) -> Unit) = EndChatViewHolder(parent.inflate(R.layout.item_chat_end), listener)
    }
}