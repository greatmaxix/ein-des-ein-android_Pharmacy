package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName

data class BaseDataResponse<T>(@SerializedName("data") val data: T)