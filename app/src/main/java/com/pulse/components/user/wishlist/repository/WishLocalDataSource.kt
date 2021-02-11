package com.pulse.components.user.wishlist.repository

import com.pulse.components.chat.model.message.MessageDAO

class WishLocalDataSource(private val chatMessageDAO: MessageDAO) {

    suspend fun updateWishMessages(globalProductId: Int, isWish: Boolean) =
        chatMessageDAO.getProductMessages(globalProductId)
            .apply {
                forEach { it.product?.wish = isWish }
                chatMessageDAO.insert(this)
            }
}