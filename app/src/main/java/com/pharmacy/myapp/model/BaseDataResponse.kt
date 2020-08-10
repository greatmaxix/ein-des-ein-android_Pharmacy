package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.core.network.ApiException

data class BaseDataResponse<T>(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: T,
    @SerializedName("error_type") val errorType: String,
    @SerializedName("violations") val violations: List<Violation>
) {

    fun isSuccess() = status == "success"

    fun dataOrThrow() = if (isSuccess()) data else throw ApiException(message, errorType, violations)
}

data class Violation(
    @SerializedName("propertyPath") val propertyPath: String,
    @SerializedName("message") val message: String
)