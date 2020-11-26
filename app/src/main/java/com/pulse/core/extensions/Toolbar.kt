package com.pulse.core.extensions

import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isEmpty

fun Toolbar.setMenu(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) {
    if (this.menu.isEmpty()) {
        inflateMenu(menu)
        setOnMenuItemClickListener(itemClick)
    }
}

val Toolbar.titleView: View
    get() = getChildAt(0)
