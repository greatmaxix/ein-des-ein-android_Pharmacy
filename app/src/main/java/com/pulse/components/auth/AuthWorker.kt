package com.pulse.components.auth

import android.content.Context
import androidx.work.WorkerParameters
import com.pulse.core.base.worker.BaseAvatarWorker

class AuthWorker(context: Context, params: WorkerParameters) : BaseAvatarWorker(context, params) {

    override suspend fun avatarUrl() = inputData.getString(AUTH_WORKER_KEY)

    companion object {
        const val AUTH_WORKER_KEY = "avatarKey"
    }
}