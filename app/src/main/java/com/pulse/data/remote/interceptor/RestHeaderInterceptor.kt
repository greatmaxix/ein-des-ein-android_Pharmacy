package com.pulse.data.remote.interceptor

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.getOnes
import com.pulse.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class RestHeaderInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = dataStore.getOnes(FIELD_ACCESS_TOKEN)

        return if (!token.isNullOrEmpty()) {
            chain.proceed(original.newBuilder().header("Authorization", "Bearer $token").build())
        } else {
            chain.proceed(original)
        }
    }
}