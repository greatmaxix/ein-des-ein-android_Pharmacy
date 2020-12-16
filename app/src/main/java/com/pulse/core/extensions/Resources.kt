package com.pulse.core.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.DrawableRes

fun Resources.getBitmapDrawableFromVectorDrawable(context: Context, @DrawableRes drawableRes: Int) = context.getCompatDrawable(drawableRes)?.let {
    BitmapDrawable(this, Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
    })
}

fun Resources.convertPxToDp(px: Float) = px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
fun Resources.convertDpToPx(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

fun Resources.getDimenByName(name: String, defPackage: String) = getIdentifier(name, "dimen", defPackage)
fun Resources.getStringByName(name: String, defPackage: String) = getIdentifier(name, "string", defPackage)
fun Resources.getDrawableByName(name: String, defPackage: String) = getIdentifier(name, "drawable", defPackage)