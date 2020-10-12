package com.pharmacy.myapp.data.remote

import com.google.gson.annotations.SerializedName

data class ErrorModel(val message: String, @SerializedName("error_type") val type: String)