package com.pharmacy.myapp.core.extensions

import android.content.Context
import android.content.res.TypedArray
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.io.InputStream

inline fun <reified T : Any> koinInstance(): T = object : KoinComponent {
    val value: T by inject()
}.value

fun File.inputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
        this.outputStream().use { out ->
            input.copyTo(out)
        }
    }
}

fun Context.assetFile(fileName: String): File = File.createTempFile(fileName, null).apply { inputStreamToFile(assets.open(fileName)) }

@Deprecated("Use @use from com.pharmacy.myapp.core.extensions")
fun TypedArray.getData(action: TypedArray.() -> Unit) {
    try {
        action.invoke(this)
    } finally {
        recycle()
    }
}