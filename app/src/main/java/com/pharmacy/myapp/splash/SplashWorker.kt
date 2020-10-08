package com.pharmacy.myapp.splash

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pharmacy.myapp.splash.repository.SplashRepository
import com.pharmacy.myapp.util.Constants
import com.pharmacy.myapp.util.ImageFileUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.io.File

@KoinApiExtension
class SplashWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params), KoinComponent {

    private val repository by inject<SplashRepository>()

    override suspend fun doWork() = withContext(IO) {
        try {
            repository.setCustomerRetrieveAvatarUrl()?.let(::saveAvatar) ?: Result.failure()
        } catch (e: Exception) {
            Timber.e(e)
            Result.failure()
        }
    }

    private fun saveAvatar(url: String): Result {
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