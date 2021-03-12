package com.pulse.components.analyzes.list.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.components.checkout.model.PaymentMethodAdapterModel
import com.pulse.data.remote.model.order.CustomerOrderData
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Analyze(
    @SerializedName("id") val id: Int,
    @SerializedName("customer") val customer: CustomerOrderData,
    @SerializedName("orderNo") val orderNo: String,
    @SerializedName("category") val category: AnalyzeCategory,
    @SerializedName("clinic") val clinic: Clinic,
    @SerializedName("dateTime") val dateTime: LocalDateTime,
    @SerializedName("discount") val discount: Int?,
    @SerializedName("totalPrice") val totalPrice: Int,
    @SerializedName("paymentType") val paymentType: PaymentMethodAdapterModel,
    @SerializedName("note") val note: String?
) : Parcelable