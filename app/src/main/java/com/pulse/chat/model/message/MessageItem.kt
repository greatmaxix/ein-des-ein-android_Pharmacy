package com.pulse.chat.model.message

import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.pulse.chat.adapter.ChatMessageAdapter
import com.pulse.model.Picture
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
@Entity(indices = [Index(value = ["messageId", "chatId"], unique = true)])
class MessageItem(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("itemId") val itemId: Int = 0,
    @ColumnInfo(name = "messageId")
    @SerializedName("id") val id: Int,
    @SerializedName("ownerUuid") val ownerUuid: String,
    @SerializedName("text") val text: String?,
    @ColumnInfo(name = "messageCreatedAt")
    @SerializedName("createdAt") val createdAt: LocalDateTime,
    @SerializedName("chatNumber") val messageNumber: Int,
    @SerializedName("chatId") val chatId: Int,
    @Embedded
    @SerializedName("file") val file: Picture? = null,
    @Embedded
    @SerializedName("globalProductCard") val product: MessageProduct? = null,
    @SerializedName("messageType") var messageType: Int? = null,
    @SerializedName("ownerMessage") var ownerMessage: Boolean? = false,
    @Embedded
    @SerializedName("recipeImage") val recipeImage: RecipeImage? = null
) : Parcelable {

    fun updateMessageType(userUuid: String?) {
        if (messageType != ChatMessageAdapter.TYPE_END_CHAT && messageType != ChatMessageAdapter.TYPE_DATE_HEADER) {
            messageType = when {
                recipeImage != null -> ChatMessageAdapter.TYPE_RECIPE
                file != null -> ChatMessageAdapter.TYPE_ATTACHMENT
                product != null -> ChatMessageAdapter.TYPE_PRODUCT
                ownerUuid == userUuid -> ChatMessageAdapter.TYPE_MESSAGE_USER
                else -> ChatMessageAdapter.TYPE_MESSAGE_PHARMACY
            }
            ownerMessage = ownerUuid == userUuid
        }
    }

    override fun toString(): String {
        return "MessageItem(id=$id, ownerUuid='$ownerUuid', text=$text, messageNumber=$messageNumber, messageType=$messageType, createdAt=${logTimeFormatter.format(createdAt)}, file=${file?.url}, product=${product?.globalProductId})"
    }

    companion object {

        private val headerDateFormatter by lazy { DateTimeFormatter.ofPattern("dd MMMM, EEEE HH:mm") }
        private val logTimeFormatter by lazy { DateTimeFormatter.ofPattern("dd MM > HH:mm:ss:SSS") }

        fun getStubItem(text: String?, messageItem: MessageItem?, type: Int, chatId: Int) = MessageItem(
            id = System.currentTimeMillis().toInt() % Integer.MAX_VALUE,
            ownerUuid = "",
            text = if (type == ChatMessageAdapter.TYPE_DATE_HEADER && messageItem?.createdAt != null) headerDateFormatter.format(messageItem.createdAt) else text,
            messageNumber = -1,
            createdAt = if (type == ChatMessageAdapter.TYPE_DATE_HEADER && messageItem?.createdAt != null) messageItem.createdAt.minusSeconds(1) else LocalDateTime.now(),
            chatId = chatId,
            messageType = type
        )
    }
}