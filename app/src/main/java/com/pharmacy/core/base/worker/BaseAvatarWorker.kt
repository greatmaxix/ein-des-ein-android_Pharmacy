package com.pharmacy.core.base.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pharmacy.util.Constants
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File


abstract class BaseAvatarWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    abstract suspend fun avatarUrl(): String?

    override suspend fun doWork() = withContext(IO) {
        try {
            avatarUrl()?.let(::saveAvatar) ?: Result.failure()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    private fun saveAvatar(url: String): Result {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) = Unit
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) = File(context.externalCacheDir, Constants.AVATAR_FILE_NAME)
                    .writeBitmap(resource, Bitmap.CompressFormat.PNG, 85)
            })
        return Result.success()
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
}