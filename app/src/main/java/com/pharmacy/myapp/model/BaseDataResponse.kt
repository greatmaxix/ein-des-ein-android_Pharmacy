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

    val isSuccess
        get() = status == "success"

    fun dataOrThrow() = if (isSuccess) data else throw ApiException(message, errorType, violations)
}

data class BaseDataResponseWithItem<T>(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: SingleItemModel<T>
)

data class Violation(
    @SerializedName("propertyPath") val propertyPath: String,
    @SerializedName("message") val message: String
)

data class PaginationModel<T>(
    @SerializedName("currentPageNumber") val currentPageNumber: Int,
    @SerializedName("items") val items: List<T>,
    @SerializedName("numItemsPerPage") val numItemsPerPage: Int,
    @SerializedName("totalCount") val totalCount: Int
)

data class SingleItemModel<T>(
    @SerializedName("item") val item: T
)

data class ListItemsModel<T>(
    @SerializedName("items") val items: List<T>
)