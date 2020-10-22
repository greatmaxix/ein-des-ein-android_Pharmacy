package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.chat.adapter.ChatMessageAdapter
import com.pulse.chat.model.ChatMessage
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_chat_end.view.*

class EndChatViewHolder(itemView: View, listener: (ChatMessageAdapter.Action) -> Unit) : BaseViewHolder<ChatMessage>(itemView) {

    init {
        with(itemView) {
            btnResumeChat.setDebounceOnClickListener { listener.invoke(ChatMessageAdapter.Action.RESUME_CHAT) }
            btnEndChat.setDebounceOnClickListener { listener.invoke(ChatMessageAdapter.Action.END_CHAT) }
        }
    }

    override fun bind(item: ChatMessage) {

    }

    companion object {

        fun newInstance(parent: ViewGroup, listener: (ChatMessageAdapter.Action) -> Unit) = EndChatViewHolder(parent.inflate(R.layout.item_chat_end), listener)
    }
}