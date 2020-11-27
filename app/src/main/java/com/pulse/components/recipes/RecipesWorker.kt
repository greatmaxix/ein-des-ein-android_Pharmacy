package com.pulse.components.recipes

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.pulse.core.extensions.downloadFile
import com.pulse.core.extensions.downloadPDF
import com.pulse.data.remote.api.RestApi
import okhttp3.ResponseBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import timber.log.Timber
import java.io.File
import java.io.IOException

class RecipesWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params), KoinComponent {

    override suspend fun doWork() = runDownloading()

    private suspend fun runDownloading() = try {
        inputData.getString(KEY_VALUE)?.let { Result.success(workDataOf(KEY_RESULT to downloadFile(get<RestApi>().downloadFile(it), it))) } ?: Result.failure()
    } catch (e: IOException) {
        Timber.e(e)
        Result.failure()
    }

    private fun downloadFile(body: ResponseBody, name: String): String {
        val displayName = "$name.pdf"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            applicationContext.contentResolver.downloadPDF(body.byteStream(), displayName, RELATIVE_PATH)
        } else {
            body.byteStream().downloadFile(displayName, PULSE_DOCUMENTS_DIR)
        }.toString()
    }

    companion object {
        const val KEY_VALUE = "value"
        const val KEY_RESULT = "result"

        private const val PULSE_DOCUMENTS_DIR = "Pulse Documents"
        private val RELATIVE_PATH = "${Environment.DIRECTORY_DOCUMENTS}${File.separator}$PULSE_DOCUMENTS_DIR"
    }
}