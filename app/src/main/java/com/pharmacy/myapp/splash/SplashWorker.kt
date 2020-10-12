package com.pharmacy.myapp.splash

import android.content.Context
import androidx.work.WorkerParameters
import com.pharmacy.myapp.core.base.worker.BaseAvatarWorker
import com.pharmacy.myapp.splash.repository.SplashRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SplashWorker(context: Context, params: WorkerParameters) : BaseAvatarWorker(context, params), KoinComponent {

    private val repository by inject<SplashRepository>()

    override suspend fun avatarUrl() = repository.setCustomerRetrieveAvatarUrl()

}