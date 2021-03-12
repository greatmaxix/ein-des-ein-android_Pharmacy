package com.pulse.data.remote.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Token.FIELD_ACCESS_TOKEN
import com.pulse.data.local.Preferences.Token.FIELD_REFRESH_TOKEN
import com.pulse.data.remote.api.RestApiRefresh
import com.pulse.model.auth.token.TokenRequest

class RestRepository(private val dataStore: DataStore<Preferences>, private val api: RestApiRefresh) {

    val accessToken
        get() = dataStore.getOnes(FIELD_ACCESS_TOKEN)

    suspend fun refreshToken() = dataStore.getOnes(FIELD_REFRESH_TOKEN)?.let {
        val newTokens = TokenRequest(it)
        val result = api.tokenRefresh(newTokens)
        dataStore.put(FIELD_ACCESS_TOKEN, result.token)
        dataStore.put(FIELD_REFRESH_TOKEN, result.refreshToken)
    } ?: throw IllegalArgumentException("refreshToken is empty")
}