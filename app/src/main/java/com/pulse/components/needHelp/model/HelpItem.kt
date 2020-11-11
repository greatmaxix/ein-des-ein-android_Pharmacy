package com.pulse.components.needHelp.model

import androidx.annotation.DrawableRes

data class HelpItem(
    @DrawableRes val icon: Int,
    val title: String,
    val text: String,
    var isExpanded: Boolean = false
)