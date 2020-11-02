package com.pulse.data.remote

import com.google.gson.annotations.SerializedName
import com.pulse.model.Violation

data class ErrorModel(val message: String, @SerializedName("error_type") val type: String, val violations: List<Violation>)