package com.pulse.user.wishlist.repository

class WishRepository(private val rds: WishRemoteDataSource, private val lds: WishLocalDataSource) {

    private suspend fun setToWishList(globalProductId: Int) = rds.setToWishList(globalProductId)
        .also { updateProductMessages(globalProductId, true) }

    private suspend fun removeFromWishList(globalProductId: Int) = rds.removeFromWishList(globalProductId)
        .also { updateProductMessages(globalProductId, false) }

    suspend fun setOrRemoveWish(setOrRemove: Pair<Boolean, Int>) = setOrRemove.run { if (first) setToWishList(second) else removeFromWishList(second) }

    suspend fun getWishList(page: Int? = null, pageSize: Int? = null) = rds.getWishList(page, pageSize)

    suspend fun updateProductMessages(globalProductId: Int, isWish: Boolean) = lds.updateWishMessages(globalProductId, isWish)
}