package com.pulse.splash

import android.content.Context
import androidx.work.WorkerParameters
import com.pulse.core.base.worker.BaseAvatarWorker
import com.pulse.user.repository.CustomerUseCase
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SplashWorker(context: Context, params: WorkerParameters) : BaseAvatarWorker(context, params), KoinComponent {

    private val repository by inject<CustomerUseCase>()

    override suspend fun avatarUrl() = repository.setCustomerRetrieveAvatarUrl()

}