package com.pulse.core.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

fun Bitmap.resize(newWidth: Int, newHeight: Int): Bitmap {
    val width = width
    val height = height
    val resized = Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply {
        postScale(newWidth.toFloat() / width, newHeight.toFloat() / height)
    }, true)
    if (resized != this) {
        recycle()
    }
    return resized
}

fun Bitmap.toByteArray(): ByteArray = ByteArrayOutputStream().apply {
    compress(Bitmap.CompressFormat.JPEG, 100, this)
}.toByteArray()
