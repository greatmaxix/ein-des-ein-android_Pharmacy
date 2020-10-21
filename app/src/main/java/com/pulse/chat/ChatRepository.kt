package com.pulse.chat

import com.pulse.data.local.SPManager
import com.pulse.data.remote.RestManager

class ChatRepository(
    private val spManager: SPManager,
    private val rm: RestManager
) {

    val isUserLoggedIn
        get() = !spManager.token.isNullOrBlank()
}