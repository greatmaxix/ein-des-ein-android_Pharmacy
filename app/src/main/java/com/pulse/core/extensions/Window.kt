package com.pulse.core.extensions

import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.Window
import androidx.annotation.ColorInt

val Window.softInputMode get() = attributes.softInputMode

fun Window.setNavigationBarColorInt(@ColorInt color: Int, withAnim: Boolean = false) {
    if (withAnim) {
        valueAnimatorMedium { navigationBarColor = navigationBarColor.mixColorWith(color, it) }
    } else {
        navigationBarColor = color
    }
}

fun Window.setStatusBarColorInt(@ColorInt color: Int, withAnim: Boolean = false) {
    if (withAnim) {
        valueAnimatorMedium { statusBarColor = statusBarColor.mixColorWith(color, it) }
    } else {
        statusBarColor = color
    }
}

fun Window.setLightStatusBar(light: Boolean) {
    decorView.systemUiVisibility = if (light) decorView.systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else 0
}