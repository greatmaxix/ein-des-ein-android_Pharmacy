package com.pulse.chat.repository

import com.pulse.data.remote.api.RestApi
import com.pulse.data.remote.model.chat.SendMessageBody
import com.pulse.data.remote.model.chat.SendReviewRequest
import okhttp3.MultipartBody

class ChatRemoteDataSource(private val ra: RestApi) {

    suspend fun messagesList(chatId: Int, pageSize: Int?, afterMessageNumber: Int?, beforeMessageNumber: Int?) =
        ra.messagesList(chatId, pageSize, afterMessageNumber, beforeMessageNumber)
            .dataOrThrow()

    suspend fun sendMessage(chatId: Int, text: String) = ra.sendMessage(chatId, SendMessageBody(text))
        .dataOrThrow()
        .item

    suspend fun sendImageMessage(chatId: Int, fileUuid: String) = ra.sendImageMessage(chatId, fileUuid)
        .dataOrThrow()
        .item

    suspend fun uploadImage(partBody: MultipartBody.Part) = ra.uploadImage(partBody)
        .dataOrThrow()
        .item

    suspend fun closeChat(chatId: Int) = ra.closeChat(chatId)
        .dataOrThrow()
        .item

    suspend fun continueChat(chatId: Int) = ra.continueChat(chatId)
        .dataOrThrow()
        .item

    suspend fun sendReview(chatId: Int, sendReviewRequest: SendReviewRequest) = ra.sendReview(chatId, sendReviewRequest)
        .dataOrThrow()
}