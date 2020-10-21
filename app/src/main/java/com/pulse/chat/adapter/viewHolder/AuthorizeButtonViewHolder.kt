package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.chat.adapter.ChatMessageAdapter
import com.pulse.chat.model.ChatMessage
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_chat_authorize.view.*

class AuthorizeButtonViewHolder(itemView: View, listener: (ChatMessageAdapter.Action) -> Unit) : BaseViewHolder<ChatMessage>(itemView) {

    init {
        itemView.btnAuthorize.setDebounceOnClickListener { listener.invoke(ChatMessageAdapter.Action.AUTHORIZE) }
    }

    override fun bind(item: ChatMessage) {
        // no op
    }

    companion object {

        fun newInstance(parent: ViewGroup, listener: (ChatMessageAdapter.Action) -> Unit) = AuthorizeButtonViewHolder(parent.inflate(R.layout.item_chat_authorize), listener)
    }
}