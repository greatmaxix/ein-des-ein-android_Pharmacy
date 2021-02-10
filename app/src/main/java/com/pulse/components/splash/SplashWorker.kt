package com.pulse.components.splash

import android.content.Context
import androidx.work.WorkerParameters
import com.pulse.components.user.repository.CustomerUseCase
import com.pulse.core.base.worker.BaseAvatarWorker
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SplashWorker(context: Context, params: WorkerParameters) : BaseAvatarWorker(context, params), KoinComponent {

    private val repository by inject<CustomerUseCase>()

    override suspend fun avatarUrl() = repository.setCustomerRetrieveAvatarUrl()

}