package com.pulse.components.analyzes.list.model

import com.google.gson.annotations.SerializedName
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.details.model.Clinic
import java.time.LocalDateTime

data class Analyze(
    @SerializedName("id") val id: Int,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("category") val category: AnalyzeCategory,
    @SerializedName("clinic") val clinic: Clinic,
    @SerializedName("dateTime") val dateTime: LocalDateTime,
    @SerializedName("totalPrice") val totalPrice: Int
)