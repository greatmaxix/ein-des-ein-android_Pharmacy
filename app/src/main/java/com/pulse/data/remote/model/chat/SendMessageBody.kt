package com.pulse.data.remote.model.chat

import com.google.gson.annotations.SerializedName

data class SendMessageBody(
    @SerializedName("text") val text: String
)