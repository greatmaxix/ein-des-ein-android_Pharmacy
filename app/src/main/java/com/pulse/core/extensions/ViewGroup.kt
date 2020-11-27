package com.pulse.core.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attach: Boolean = false) = context.inflate(layoutRes, this, attach)

fun ViewGroup.getDimension(@DimenRes dimenResId: Int) = resources.getDimension(dimenResId)

fun ViewGroup.getDimensionPixelSize(@DimenRes dimenResId: Int) = resources.getDimensionPixelSize(dimenResId)

val ViewGroup.lastChild: View
    get() = getChildAt(childCount - 1)
