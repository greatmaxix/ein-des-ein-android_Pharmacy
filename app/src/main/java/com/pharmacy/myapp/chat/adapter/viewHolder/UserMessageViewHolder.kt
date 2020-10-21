package com.pharmacy.myapp.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_message_user.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserMessageViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    override fun bind(item: MessageItem) {
        with(itemView) {
            tvMessageChat.text = item.text
//            tvReadTimeChat.text = message.readDate?.toReadDate() TODO
//            tvReadTimeChat.visibleOrGone(message.readDate != null)
        }
    }

    private fun LocalDateTime.toReadDate() = itemView.context.getString(R.string.readDateHolder, DateTimeFormatter.ofPattern("HH:mm").format(this))

    companion object {

        fun newInstance(parent: ViewGroup) = UserMessageViewHolder(parent.inflate(R.layout.item_chat_message_user))
    }
}