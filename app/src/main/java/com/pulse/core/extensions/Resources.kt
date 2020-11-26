package com.pulse.core.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes

fun Resources.lazyDimension(@DimenRes resId: Int) = lazyNotSynchronized { getDimension(resId) }

fun Resources.lazyDimensionPixelSize(@DimenRes resId: Int) = lazyNotSynchronized { getDimensionPixelSize(resId) }

fun Resources.getBitmapDrawableFromVectorDrawable(@DrawableRes drawableRes: Int) = getDrawable(drawableRes, null).let {
    BitmapDrawable(this, Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
    })
}
