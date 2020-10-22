package com.pulse.core.extensions

import android.content.Context
import android.content.res.TypedArray
import java.io.File
import java.io.InputStream

fun File.inputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
        this.outputStream().use { out ->
            input.copyTo(out)
        }
    }
}

fun Context.assetFile(fileName: String): File = File.createTempFile(fileName, null).apply { inputStreamToFile(assets.open(fileName)) }

@Deprecated("Use @use from com.pulse.core.extensions")
fun TypedArray.getData(action: TypedArray.() -> Unit) {
    try {
        action.invoke(this)
    } finally {
        recycle()
    }
}