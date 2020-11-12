package com.pulse.data.remote.model.chat

import com.google.gson.annotations.SerializedName

data class CreateChatRequest(@SerializedName("type") val type: String)