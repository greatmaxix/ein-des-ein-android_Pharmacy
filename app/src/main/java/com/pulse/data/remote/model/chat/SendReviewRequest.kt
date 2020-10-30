package com.pulse.data.remote.model.chat

import com.google.gson.annotations.SerializedName

data class SendReviewRequest(
    @SerializedName("evaluatingRating") val rating: Int,
    @SerializedName("evaluatingTags") val tags: List<String>?,
    @SerializedName("evaluatingComment") val comment: String?
)