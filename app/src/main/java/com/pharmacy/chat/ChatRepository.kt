package com.pharmacy.chat

import com.pharmacy.data.local.SPManager
import com.pharmacy.data.remote.RestManager

class ChatRepository(
    private val spManager: SPManager,
    private val rm: RestManager
) {

    val isUserLoggedIn
        get() = !spManager.token.isNullOrBlank()
}