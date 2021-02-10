package com.pulse.components.chatType.repository

import com.pulse.data.local.SPManager

class ChatTypeLocalDataSource(private val sp: SPManager) {

    var openedChatId: Int? = null
        get() = sp.openedChatId
        set(value) {
            field = value
            sp.openedChatId = value
        }
}