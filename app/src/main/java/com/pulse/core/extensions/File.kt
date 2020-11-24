package com.pulse.core.extensions

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.*

fun File.getMultipartBody(fileName: String): MultipartBody.Part {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(absolutePath)
    val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
    val fileRequestBody = asRequestBody(fileType?.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fileName, name, fileRequestBody)
}

@RequiresApi(Build.VERSION_CODES.Q)
fun ContentResolver.downloadPDF(inputStream: InputStream, displayName: String, relativePath: String) = downloadFile(inputStream, ContentValues().apply {
    put(MediaStore.MediaColumns.MIME_TYPE, "files/pdf")
    put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
    put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
})

@RequiresApi(Build.VERSION_CODES.Q)
fun ContentResolver.downloadFile(inputStream: InputStream, values: ContentValues) =
    insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), values)?.also {
        openOutputStream(it).use { out ->
            try {
                inputStream.write(out).close()
            } catch (e: IOException) {
                Timber.e(e)
                delete(it, null, null)
            }
        }
        values.clear()
        update(it, values, null, null)
    }

fun InputStream.downloadFile(displayName: String, relativePath: String): File? {
    val directory = relativePath.pathToDirectoryFile
    val newFile = directory.directoryToFile(displayName)

    newFile.outputStream().use {
        return try {
            write(it).close()
            newFile
        } catch (e: IOException) {
            newFile.delete()
            null
        }
    }
}

val String.pathToDirectoryFile
    get() = File(this)
        .apply {
            if (!isDirectory || !exists()) {
                mkdirs()
            }
        }

fun File.directoryToFile(displayName: String) = File(this, displayName)
    .apply {
        if (!exists()) {
            createNewFile()
        }
    }

@Throws(IOException::class)
fun InputStream.write(outputStream: OutputStream?): InputStream {
    val buffer = ByteArray(8 * 1024)
    var bytesRead: Int
    while (read(buffer).also { bytesRead = it } > 0) {
        outputStream?.write(buffer, 0, bytesRead)
    }
    return this
}

@Throws(OutOfMemoryError::class)
fun InputStream.bytesFromStream() = ByteArrayOutputStream()
    .run {
        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        while (read(buffer).also { bytesRead = it } != -1) {
            write(buffer, 0, bytesRead)
        }
        toByteArray()
    }
