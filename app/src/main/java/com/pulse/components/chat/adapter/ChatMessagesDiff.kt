package com.pulse.components.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pulse.components.chat.model.message.MessageItem

object ChatMessagesDiff : DiffUtil.ItemCallback<MessageItem>() {

    override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem) =
        oldItem.id == newItem.id
                && oldItem.createdAt.isEqual(newItem.createdAt)
                && oldItem.product?.wish == newItem.product?.wish

    override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem) = true
}