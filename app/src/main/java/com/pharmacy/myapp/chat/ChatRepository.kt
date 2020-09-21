package com.pharmacy.myapp.chat

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class ChatRepository(
    private val spManager: SPManager,
    private val rm: RestManager
) {

    val isUserLoggedIn
        get() = !spManager.token.isNullOrBlank()
}