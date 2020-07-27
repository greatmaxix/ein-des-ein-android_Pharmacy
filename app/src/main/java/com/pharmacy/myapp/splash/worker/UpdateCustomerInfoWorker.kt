package com.pharmacy.myapp.splash.worker

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.splash.SplashRepository
import com.pharmacy.myapp.util.Constants.Companion.AVATAR_FILE_NAME
import com.pharmacy.myapp.util.ImageFileUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

class UpdateCustomerInfoWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val repository by inject<SplashRepository>()

    override suspend fun doWork() = withContext(IO) {
        when (val avatarUrl = repository.updateCustomerInfo()) {
            is Success -> {
                saveAvatarToFile(avatarUrl)
                Result.success()
            }
            is Error -> Result.failure()
        }
    }

    private fun saveAvatarToFile(avatarUrl: Success<String>) {
        val target = object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val file = File(context.externalCacheDir, AVATAR_FILE_NAME)
                ImageFileUtil.saveImageDrawable(resource, file)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

        }
        Glide.get(context).clearDiskCache()
        Glide.with(context).asDrawable().load(avatarUrl.value).into(target)
    }

}