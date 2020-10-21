package com.pharmacy.myapp.chatType.repository

import com.pharmacy.myapp.data.local.SPManager

class ChatTypeLocalDataSource(private val sp: SPManager) {

    var openedChatId: Int? = null
        get() = sp.openedChatId
        set(value) {
            field = value
            sp.openedChatId = value
        }
}