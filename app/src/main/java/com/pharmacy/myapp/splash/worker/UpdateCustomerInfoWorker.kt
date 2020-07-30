package com.pharmacy.myapp.splash.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.splash.SplashRepository
import com.pharmacy.myapp.util.AvatarUtil.saveAvatarToFile
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class UpdateCustomerInfoWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val repository by inject<SplashRepository>()

    override suspend fun doWork() = withContext(IO) {
        when (val avatarUrl = repository.updateCustomerInfo()) {
            is Success -> {
                val url = avatarUrl.value
                if (!url.isNullOrEmpty()) saveAvatarToFile(context, url) else Result.failure()
                Result.success()
            }
            is Error -> Result.failure()
        }
    }

}