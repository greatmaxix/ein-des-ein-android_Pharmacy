package com.pharmacy.myapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.exifinterface.media.ExifInterface
import timber.log.Timber
import java.io.*

object ImageFileUtil {

    private const val MAX_IMAGE_SIDE_SIZE = 300
    private const val MAX_IMAGE_FILE_SIZE = 5 * 1024 * 1024

    @SuppressLint("ExifInterface")
    @WorkerThread
    @Throws(IOException::class)
    fun compressImage(context: Context, src: File, imageUri: Uri) {
        val optionOriginalBmp = BitmapFactory.Options()
        var bitmap: Bitmap? = BitmapFactory.decodeFile(src.absolutePath)

        var pictureOrientation = -1
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(imageUri)
            inputStream?.let {
                val exifInterface = ExifInterface(it)
                pictureOrientation = exifInterface.getAttributeInt(
                    android.media.ExifInterface.TAG_ORIENTATION,
                    android.media.ExifInterface.ORIENTATION_NORMAL
                )
            }
        } catch (e: IOException) {
            Timber.e(e, "ExifInterface exception")
        } finally {
            inputStream?.close()
        }

        if (bitmap != null) {
            if (bitmap.width >= MAX_IMAGE_SIDE_SIZE && bitmap.height >= MAX_IMAGE_SIDE_SIZE) {
                val bitmapOptions = BitmapFactory.Options()
                bitmapOptions.inSampleSize = 1
                while (bitmap!!.width >= MAX_IMAGE_SIDE_SIZE && bitmap.height >= MAX_IMAGE_SIDE_SIZE) {
                    bitmapOptions.inSampleSize++
                    bitmap = BitmapFactory.decodeFile(src.absolutePath, bitmapOptions)
                }
                Timber.d("Resize: %s", bitmapOptions.inSampleSize)
            }
        } else {
            if (optionOriginalBmp.outWidth >= MAX_IMAGE_SIDE_SIZE && optionOriginalBmp.outHeight >= MAX_IMAGE_SIDE_SIZE) {
                val bmpOptions = BitmapFactory.Options()
                bmpOptions.inSampleSize = calculateInSampleSize(optionOriginalBmp, 512, 512)
                bitmap = BitmapFactory.decodeFile(src.absolutePath, bmpOptions)
                Timber.d("Resize: %s", bmpOptions.inSampleSize)
            } else {
                val bmpOptions = BitmapFactory.Options()
                bmpOptions.inSampleSize = 2
                bitmap = BitmapFactory.decodeFile(src.absolutePath, bmpOptions)
            }
        }

        var compressQuality = 104 // quality decreasing by 5 every loop. (start from 99)
        var streamLength = MAX_IMAGE_FILE_SIZE
        while (streamLength >= MAX_IMAGE_FILE_SIZE) {
            val bmpStream = ByteArrayOutputStream()
            compressQuality -= 5
            Timber.d("Quality: %s", compressQuality)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            Timber.d("Size: %s", streamLength)
        }

        // exif rotate
        val rotatedBitmap = rotateBitmap(bitmap!!, pictureOrientation)

        val byteArrayOutputStream = ByteArrayOutputStream()
        if (rotatedBitmap != null) {
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, byteArrayOutputStream)
            rotatedBitmap.recycle()
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, byteArrayOutputStream)
            bitmap.recycle()
        }

        val bitmapData = byteArrayOutputStream.toByteArray()
        val out = FileOutputStream(src)
        out.write(bitmapData)
        out.flush()
        out.close()
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }

    fun cropRect(src: File) {
        val bitmap: Bitmap? = BitmapFactory.decodeFile(src.absolutePath)
        var destination: Bitmap?

        bitmap?.let { originalBitmap ->
            if (originalBitmap.width >= originalBitmap.height) {
                destination = Bitmap.createBitmap(
                    originalBitmap,
                    originalBitmap.width / 2 - originalBitmap.height / 2,
                    0,
                    originalBitmap.height,
                    originalBitmap.height
                )
            } else {
                destination = Bitmap.createBitmap(
                    originalBitmap,
                    0,
                    originalBitmap.height / 2 - originalBitmap.width / 2,
                    originalBitmap.width,
                    originalBitmap.width
                )
            }
            destination?.let { saveImageBitmapToFile(it, src) }
        }
    }

    fun saveImageByUriToFile(context: Context, destination: File, uri: Uri) {
        val bitmap = decodeBitmap(context, uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        bitmap.recycle()

        val bitmapData = byteArrayOutputStream.toByteArray()
        saveByteArrayToFile(bitmapData, destination)
    }

    private fun saveImageBitmapToFile(bitmap: Bitmap, destination: File) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        bitmap.recycle()

        val bitmapData = byteArrayOutputStream.toByteArray()
        saveByteArrayToFile(bitmapData, destination)
    }

    private fun saveByteArrayToFile(array: ByteArray, destination: File) {
        val out = FileOutputStream(destination)
        out.write(array)
        out.flush()
        out.close()
    }

    private fun decodeBitmap(context: Context, uri: Uri): Bitmap {
        val options = BitmapFactory.Options()
        var fileDescriptor: AssetFileDescriptor? = null
        try {
            fileDescriptor = context.contentResolver.openAssetFileDescriptor(uri, "r")
        } catch (e: FileNotFoundException) {
            Timber.e(e, "Error getting file")
        }

        return BitmapFactory.decodeFileDescriptor(fileDescriptor!!.fileDescriptor, null, options)
    }

    fun saveImageDrawable(resource: Drawable, destination: File) {
        val bitmap = drawableToBitmap(resource)
        saveImageBitmapToFile(bitmap, destination)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}