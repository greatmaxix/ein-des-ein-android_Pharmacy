package com.pharmacy.myapp.chat.adapter.viewHolder

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.model.ChatMessage
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
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