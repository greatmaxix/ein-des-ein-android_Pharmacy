package com.pulse.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Chat.FIELD_IS_CHAT_FOREGROUND
import com.pulse.data.remote.api.RestApi

class MainRepository(private val dataStore: DataStore<Preferences>, private val restApi: RestApi, private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    fun customerLiveData() = dao.customerLiveData()

    suspend fun setChatForeground(isForeground: Boolean) {
        dataStore.put(FIELD_IS_CHAT_FOREGROUND, isForeground)
    }

    suspend fun getChat(chatId: Int) = restApi.getChat(chatId)
        .dataOrThrow()
        .item
}