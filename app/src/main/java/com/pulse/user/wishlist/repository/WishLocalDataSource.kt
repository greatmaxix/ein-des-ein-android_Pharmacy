package com.pulse.user.wishlist.repository

import com.pulse.chat.model.message.MessageDAO

class WishLocalDataSource(private val chatMessageDAO: MessageDAO) {

    suspend fun updateWishMessages(globalProductId: Int, isWish: Boolean) {
        chatMessageDAO.getProductMessages(globalProductId)
            .apply {
                forEach { it.product?.wish = isWish }
                chatMessageDAO.insert(this)
            }
    }
}