package com.pulse.components.chat.model.chat

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pulse.R
import com.pulse.components.chat.model.message.MessageItem
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity
data class ChatItem(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("topicName") val topicName: String,
    @Embedded
    @SerializedName("customer") val customer: UserShort,
    @SerializedName("status") val status: String,
    @ColumnInfo(name = "chatType")
    @SerializedName("type") val type: String,
    @Embedded
    @SerializedName("lastMessage") val lastMessage: MessageItem?,
    @SerializedName("createdAt") val createdAt: LocalDateTime?,
    @SerializedName("isAutomaticClosed") val isAutomaticClosed: Boolean
) : Parcelable {

    val isCloseRequest: Boolean
        get() = status == STATUS_CLOSE_REQUEST

    val isClosed: Boolean
        get() = status == STATUS_CLOSED

    val isClosedByTimer
        get() = isAutomaticClosed && isClosed

    val typeNameResId
        get() = if (type == TYPE_PHARMACIST) R.string.pharmacistType else R.string.doctorType

    companion object {

        const val STATUS_CLOSE_REQUEST = "close_request"
        const val STATUS_CLOSED = "closed"

        const val TYPE_PHARMACIST = "pharmacist"
    }
}