package com.pulse.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.chat.model.ChatMessage
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import kotlinx.android.synthetic.main.item_chat_date_header.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateHeaderViewHolder(itemView: View) : BaseViewHolder<ChatMessage>(itemView) {

    override fun bind(item: ChatMessage) {
        itemView.tvDateChat.text = item.asDateHeader().date.toChatDate().capitalize()
    }

    private fun LocalDateTime.toChatDate() = DateTimeFormatter.ofPattern("EEEE HH:mm").format(this)

    companion object {

        fun newInstance(parent: ViewGroup) = DateHeaderViewHolder(parent.inflate(R.layout.item_chat_date_header))
    }
}