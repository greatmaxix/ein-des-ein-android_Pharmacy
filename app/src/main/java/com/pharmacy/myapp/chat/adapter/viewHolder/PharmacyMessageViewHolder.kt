package com.pharmacy.myapp.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.model.ChatMessage
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_message_pharmacy.view.*

class PharmacyMessageViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    override fun bind(item: ChatMessage) {
        itemView.tvMessageChat.text = item.asPharmacyMessage().message
    }

    companion object {

        fun newInstance(parent: ViewGroup) = PharmacyMessageViewHolder(parent.inflate(R.layout.item_chat_message_pharmacy))
    }
}