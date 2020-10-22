package com.pulse.data.remote.model.chat

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pulse.chat.model.message.MessageItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatItem(
    @SerializedName("id") val id: Int,
    @SerializedName("topicName") val topicName: String,
    @SerializedName("customer") val customer: UserShort,
    @SerializedName("status") val status: String,
    @SerializedName("mark") val mark: Int,
    @SerializedName("type") val type: String,
    @SerializedName("user") val user: UserShort,
    @SerializedName("lastMessage") val lastMessage: MessageItem?
) : Parcelable {

    companion object {
        const val STATUS_OPENED = "opened"
        const val STATUS_ANSWERED = "answered"
        const val STATUS_CLOSE_REQUEST = "close_request"
        const val STATUS_CLOSED = "closed"
        const val STATUS_RATED = "rated"
    }
}