package com.pulse.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.chat.adapter.ChatMessageAdapter
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.ItemChatEndBinding

class EndChatViewHolder(itemView: View, listener: (ChatMessageAdapter.Action) -> Unit) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatEndBinding::bind)

    init {
        with(binding) {
            mbResumeChat.setDebounceOnClickListener { listener(ChatMessageAdapter.Action.RESUME_CHAT) }
            mbEndChat.setDebounceOnClickListener { listener(ChatMessageAdapter.Action.END_CHAT) }
        }
    }

    override fun bind(item: MessageItem) {
        // no op
    }

    companion object {

        fun newInstance(parent: ViewGroup, listener: (ChatMessageAdapter.Action) -> Unit) = EndChatViewHolder(parent.inflate(R.layout.item_chat_end), listener)
    }
}