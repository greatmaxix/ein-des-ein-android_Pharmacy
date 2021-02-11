package com.pulse.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.databinding.ItemChatMessageUserBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserMessageViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatMessageUserBinding::bind)

    override fun bind(item: MessageItem) {
        with(binding) {
            mtvMessage.text = item.text
//            tvReadTimeChat.text = message.readDate?.toReadDate() TODO
//            tvReadTimeChat.visibleOrGone(message.readDate != null)
        }
    }

    private fun LocalDateTime.toReadDate() = itemView.context.getString(R.string.readDateHolder, DateTimeFormatter.ofPattern("HH:mm").format(this))

    companion object {

        fun newInstance(parent: ViewGroup) = UserMessageViewHolder(parent.inflate(R.layout.item_chat_message_user))
    }
}