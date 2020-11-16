package com.pulse.components.recipes.model

import com.google.gson.annotations.SerializedName
import com.pulse.model.Picture

data class Doctor(@SerializedName("name") val name: String?, @SerializedName("avatar") val avatar: Picture?)