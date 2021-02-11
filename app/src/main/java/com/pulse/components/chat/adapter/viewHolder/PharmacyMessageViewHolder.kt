package com.pulse.components.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.databinding.ItemChatMessagePharmacyBinding

class PharmacyMessageViewHolder(itemView: View) : BaseViewHolder<MessageItem>(itemView) {

    private val binding by viewBinding(ItemChatMessagePharmacyBinding::bind)

    override fun bind(item: MessageItem) {
        binding.mtvMessage.text = item.text
    }

    companion object {

        fun newInstance(parent: ViewGroup) = PharmacyMessageViewHolder(parent.inflate(R.layout.item_chat_message_pharmacy))
    }
}