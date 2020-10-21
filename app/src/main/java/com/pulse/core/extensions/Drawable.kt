package com.pulse.core.extensions

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import androidx.annotation.ColorInt

fun Drawable.setFilter(@ColorInt color: Int?) = when {
    color == null -> colorFilter = null
    SDK_INT >= Q -> colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    else -> setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}