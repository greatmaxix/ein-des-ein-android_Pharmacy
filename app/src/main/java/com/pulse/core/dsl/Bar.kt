package com.pulse.core.dsl

import androidx.annotation.ColorRes
import com.pulse.R

data class Bar(
    @ColorRes var statusBarColorRes: Int? = R.color.colorGlobalWhite,
    @ColorRes var navigationBarColorRes: Int? = R.color.colorGlobalWhite,
    var lightStatusBar: Boolean? = null
)