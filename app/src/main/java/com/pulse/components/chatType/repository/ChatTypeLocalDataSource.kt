package com.pulse.components.chatType.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Chat.FIELD_OPENED_CHAT_ID

class ChatTypeLocalDataSource(private val dataStore: DataStore<Preferences>) {

    suspend fun setOpenedChatId(id: Int) = dataStore.put(FIELD_OPENED_CHAT_ID, id)
}