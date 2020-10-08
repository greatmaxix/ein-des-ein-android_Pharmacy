package com.pharmacy.myapp.core.base.worker

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pharmacy.myapp.util.Constants
import com.pharmacy.myapp.util.ImageFileUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

abstract class BaseAvatarWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    abstract val avatarUrl: String?

    override suspend fun doWork() = withContext(IO) {
        try {
            avatarUrl?.let(::saveAvatar) ?: Result.failure()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    private fun saveAvatar(url: String): Result {
        Glide.get(context).clearDiskCache()
        Glide.with(context)
            .asDrawable()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean) = true
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    resource?.let { ImageFileUtil.saveImageDrawable(it, File(context.externalCacheDir, Constants.AVATAR_FILE_NAME)) }
                    return true
                }
            })
            .load(url)
            .submit()
        return Result.success()
    }
}