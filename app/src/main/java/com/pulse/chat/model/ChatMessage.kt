package com.pulse.chat.model

import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_ATTACHMENT
import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_AUTH_BUTTON
import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_DATE_HEADER
import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_END_CHAT
import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_MESSAGE_PHARMACY
import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_MESSAGE_USER
import com.pulse.chat.adapter.ChatMessageAdapter.Companion.TYPE_PRODUCT
import com.pulse.product.model.TempRecommendedModel
import java.time.LocalDateTime

sealed class ChatMessage(val itemType: Int) {

    class UserMessage(
        val message: String,
        val readDate: LocalDateTime? = null
    ) : ChatMessage(TYPE_MESSAGE_USER)

    fun asUserMessage() = this as UserMessage

    class PharmacyMessage(
        val message: String
    ) : ChatMessage(TYPE_MESSAGE_PHARMACY)

    fun asPharmacyMessage() = this as PharmacyMessage

    class DateHeader(
        val date: LocalDateTime
    ) : ChatMessage(TYPE_DATE_HEADER)

    fun asDateHeader() = this as DateHeader

    class Attachment(
        val items: MutableList<String> // TODO change type of content
    ) : ChatMessage(TYPE_ATTACHMENT)

    fun asAttachment() = this as Attachment

    class Product(
        val product: TempRecommendedModel // TODO change type of content
    ) : ChatMessage(TYPE_PRODUCT)

    fun asProduct() = this as Product

    object AuthorizeButton : ChatMessage(TYPE_AUTH_BUTTON)

    object EndChat : ChatMessage(TYPE_END_CHAT)
}