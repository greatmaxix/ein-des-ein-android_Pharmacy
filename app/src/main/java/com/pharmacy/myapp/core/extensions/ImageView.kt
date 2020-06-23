package com.pharmacy.myapp.core.extensions

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

fun ImageView.setFilter(@ColorInt color: Int) {
    drawable?.setFilter(color)
}

fun ImageView.setFilterRes(@ColorRes colorRes: Int) {
    val color = context.getColor(colorRes)
    setFilter(color)
}
