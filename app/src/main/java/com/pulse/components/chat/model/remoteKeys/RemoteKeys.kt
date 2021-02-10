package com.pulse.components.chat.model.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pulse.components.chat.model.message.MessageItem

@Entity
data class RemoteKeys(
    @PrimaryKey val messageId: Int,
    val chatId: Int,
    val prevNumber: Int?,
    val nextNumber: Int?
) {

    val isEmpty: Boolean
        get() = messageId == 0 && chatId == 0

    val isError: Boolean
        get() = messageId == -1 && chatId == -1

    companion object {

        fun emptyInstance() = RemoteKeys(0, 0, null, null)

        fun errorInstance() = RemoteKeys(-1, -1, null, null)

        fun createRemoteKey(message: MessageItem): RemoteKeys {
            val prevNumber = if (message.messageNumber <= 1) null else message.messageNumber - 1
            return RemoteKeys(messageId = message.id, chatId = message.chatId, prevNumber = prevNumber, nextNumber = message.messageNumber + 1)
        }
    }
}