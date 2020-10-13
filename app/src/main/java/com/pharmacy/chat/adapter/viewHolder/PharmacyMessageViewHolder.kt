package com.pharmacy.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.chat.model.ChatMessage
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_message_pharmacy.view.*

class PharmacyMessageViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    override fun bind(item: ChatMessage) {
        itemView.tvMessageChat.text = item.asPharmacyMessage().message
    }

    companion object {

        fun newInstance(parent: ViewGroup) = PharmacyMessageViewHolder(parent.inflate(R.layout.item_chat_message_pharmacy))
    }
}