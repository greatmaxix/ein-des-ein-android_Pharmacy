package com.pharmacy.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.chat.model.ChatMessage
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.inflate
import com.pharmacy.core.extensions.visibleOrGone
import kotlinx.android.synthetic.main.item_chat_message_user.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserMessageViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    override fun bind(item: ChatMessage) {
        with(itemView) {
            val message = item.asUserMessage()
            tvMessageChat.text = message.message
            tvReadTimeChat.text = message.readDate?.toReadDate()
            tvReadTimeChat.visibleOrGone(message.readDate != null)
        }
    }

    private fun LocalDateTime.toReadDate() = itemView.context.getString(R.string.readDateHolder, DateTimeFormatter.ofPattern("HH:mm").format(this))

    companion object {

        fun newInstance(parent: ViewGroup) = UserMessageViewHolder(parent.inflate(R.layout.item_chat_message_user))
    }
}